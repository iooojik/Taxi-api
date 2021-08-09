package octii.dev.taxi.contollers.sockets

import octii.dev.taxi.listeners.WebSocketEventListener
import octii.dev.taxi.constants.MessageType
import octii.dev.taxi.models.sockets.OrdersResponseModel
import octii.dev.taxi.models.database.*
import octii.dev.taxi.services.*
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class OrderController(val simpMessagingTemplate : SimpMessagingTemplate,
                      val userService: UserService, val driverService: DriverService,
                      val ordersService: OrdersService, val rejectedOrdersService: RejectedOrdersService,
                      val coordinateService: CoordinateService, val taximeterService: TaximeterService) {

    val logger = WebSocketEventListener.logger

    @MessageMapping("/order.make.{uuid}")//пользователь создал заказ
    fun makeOrder(orderModel: OrdersModel = OrdersModel(),
                  @Payload c : UserModel, @DestinationVariable("uuid") customerUUID : String){
        println("order was made")
        //ищем пользователя в таблице
        val customer = userService.getByPhoneNumber(c.phone)

        if (customer != null) {
            var order : OrdersModel? = null
            //если метод вызывается не повторно после отказа водителя, то создаём новый заказ, иначе ищем в списке заказов
            if (orderModel.isNew) {
                //создаём заказ в таблице orders
                order = ordersService.registerNewOrder(
                    OrdersModel(
                        customerID = customer.id,
                        uuid = UUID.randomUUID().toString(), customer = customer
                    )
                )
            } else if (orderModel.uuid.isNotEmpty()){
                order = ordersService.getByOrderUUID(orderModel.uuid)
            }

            if (order != null) {
                val orderUUID = order.uuid
                //получение подходящего водителя
                val foundDriver = getNearestDriver(customer, orderUUID)
                println(foundDriver?.driver == null)
                if (foundDriver?.driver != null) {
                    //обновляем информацию о заказе
                    order.driverID = foundDriver.driver!!.id
                    order.driver = foundDriver.driver
                    order = ordersService.update(order)
                    //отправляем найденному водителю предложение о заказе
                    println("uuid: ${foundDriver.driver!!.uuid}")
                    simpMessagingTemplate.convertAndSend(
                        "/topic/${foundDriver.driver!!.uuid}",
                        OrdersResponseModel(MessageType.ORDER_REQUEST, order = order)
                    )
                    logger.info("order: $order")
                } else {
                    simpMessagingTemplate.convertAndSend(
                        "/topic/${customer.uuid}", OrdersResponseModel(MessageType.NO_ORDERS, order = order)
                    )
                }
            } else {
                simpMessagingTemplate.convertAndSend(
                    "/topic/${customerUUID}", OrdersResponseModel(MessageType.NO_ORDERS, order = orderModel)
                )
            }
        } else {
            simpMessagingTemplate.convertAndSend(
                "/topic/${customerUUID}", OrdersResponseModel(MessageType.NO_ORDERS, order = orderModel)
            )
        }
    }

    @MessageMapping("/order.accept.{uuid}") //водитель принял заказ
    fun acceptOrder(@Payload orderModel: OrdersModel, @DestinationVariable("uuid") driverUUID : String){
        println("order accepted")
        val order = ordersService.getByOrderUUID(orderModel.uuid)
        println(driverUUID)
        if (order != null) {
            val driver = userService.getByUserUUID(driverUUID)

            order.driverID = driver?.id
            order.driver = driver!!
            ordersService.update(order)
            println(order.driver)
            println(order.customer)

            simpMessagingTemplate.convertAndSend(
                "/topic/${order.driver!!.uuid}",
                OrdersResponseModel(MessageType.ORDER_ACCEPT, order = order)
            )

            simpMessagingTemplate.convertAndSend(
                "/topic/${order.customer!!.uuid}",
                OrdersResponseModel(MessageType.ORDER_ACCEPT, order = order)
            )
            println("sent")
        }
    }

    @MessageMapping("/order.reject.{uuid}") //водитель отказался от заказа
    fun rejectOrder(@Payload orderModel: OrdersModel, @DestinationVariable("uuid") driverUUID : String){
        println("order rejected")
        val order = ordersService.getByOrderUUID(orderModel.uuid)
        if (order != null) {
            rejectedOrdersService.reject(RejectedOrdersModel(driverUUID = driverUUID, orderUuid = order.uuid))
            order.isNew = false
            ordersService.update(order)
            makeOrder(order, order.customer!!, order.customer!!.uuid)
        }
    }

    @MessageMapping("/order.finish.{uuid}") //водитель отказался от заказа
    fun finishOrder(@Payload orderModel: OrdersModel, @DestinationVariable("uuid") driverUUID : String){
        println("order finished")
        var order = ordersService.getByOrderUUID(orderModel.uuid)
        if (order != null) {
            order.isFinished = true
            order = ordersService.update(order)
            
            simpMessagingTemplate.convertAndSend(
                "/topic/${orderModel.driver!!.uuid}",
                OrdersResponseModel(MessageType.ORDER_FINISHED, order = order)
            )
            simpMessagingTemplate.convertAndSend(
                "/topic/${orderModel.customer!!.uuid}",
                OrdersResponseModel(MessageType.ORDER_FINISHED, order = order)
            )
        }
    }

    @MessageMapping("/navigation.coordinates.update.{uuid}") //обновление координат
    fun updateCoordinates(@Payload coordinates : CoordinatesModel, @DestinationVariable("uuid") userUUID : String){
        val user = userService.getByUserUUID(userUUID)
        if (user != null){
            //coordinates.userId = user.id
            coordinateService.update(coordinates, user.id)
        }
        /*
        simpMessagingTemplate.convertAndSend(
            "/topic/$userUUID",
            OrdersResponseModel(MessageType.COORDINATES_UPDATE, coordinates)
        )*/
    }

    private fun getNearestDriver(customer : UserModel, orderUUID: String) : DriverModel? {
        //получаем список доступных водителей
        val availableDrivers = driverService.getAll()
        logger.info("available: $availableDrivers")
        //получаем список водителей, которые отказались от выполнения заказа
        val rejectedOrders = rejectedOrdersService.getByOrderUUID(orderUUID)
        //подходящие водители
        val map : HashMap<Double, DriverModel> = hashMapOf()

        for (driverAv in availableDrivers){
            val driver = driverAv.driver
            //проверяем, отказал ли водитель в выполнении заказа
            var wasFoundRejected = false
            for (order in rejectedOrders){
                if (driver?.uuid == order.driverUUID) wasFoundRejected = true
            }
            //если не отказал, то проверяем расстояние между клиентом и водителем
            if (!wasFoundRejected && driver != null){
                val driverCoordinates = coordinateService.getByUserId(driver.id)
                val customerCoordinates = coordinateService.getByUserId(customer.id)
                if (driverCoordinates != null && customerCoordinates != null) {
                    //рассчитываем дистанцию между клиентом и водителем
                    val distance = calcDistance(
                        customerCoordinates.latitude, customerCoordinates.longitude,
                        driverCoordinates.latitude, driverCoordinates.longitude
                    )
                    if (distance <= driverAv.rideDistance) map[distance] = driverAv
                    logger.info("distance: $distance ${driver.id}")
                }
            }

        }
        //ищем подходящего водителя, сортируя сначала по дистанции, а потом по цене за минуту
        val comparator = compareBy<Pair<Double, DriverModel>>{it.first}
            .thenComparator { a: Pair<Double, DriverModel>, b: Pair<Double, DriverModel> ->
                compareValues(
                    a.first,
                    b.second.prices?.pricePerMinute
                )
            }
        //получаем первое значение - это и есть подходящий водитель
        val list = map.toList().sortedWith(comparator)
        logger.info("found: $map")
        return if (list.isNotEmpty())
            list[0].second
        else {
            null
        }
    }

    fun calcDistance(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double): Double {
        val earthRad = 6371.0
        val lat1r = deg2rad(lat1)
        val lon1r = deg2rad(lon1)
        val lat2r = deg2rad(lat2)
        val lon2r = deg2rad(lon2)
        val u = Math.sin((lat2r - lat1r)/2)
        val v = Math.sin((lon2r - lon1r)/2)
        return 2.0 * earthRad * Math.asin(Math.sqrt(u * u + Math.cos(lat1r) * Math.cos(lat2r) * v * v))
    }

    /* The function to convert decimal into radians */
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    /* The function to convert radians into decimal */
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}