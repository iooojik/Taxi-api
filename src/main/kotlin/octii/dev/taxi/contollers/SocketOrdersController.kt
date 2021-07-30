package octii.dev.taxi.contollers

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import octii.dev.taxi.listeners.WebSocketEventListener
import octii.dev.taxi.models.*
import octii.dev.taxi.services.DriverAvailableService
import octii.dev.taxi.services.OrdersService
import octii.dev.taxi.services.RejectedOrdersService
import octii.dev.taxi.services.UserService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


@Controller
class SocketOrdersController(val simpMessagingTemplate : SimpMessagingTemplate,
                             val userService: UserService, val driverAvailableService: DriverAvailableService,
                             val ordersService: OrdersService, val rejectedOrdersService: RejectedOrdersService) {

    val logger = WebSocketEventListener.logger

    @MessageMapping("/order.make.{uuid}")//пользователь создал заказ
    fun makeOrder(//isNew : Boolean = false, orderModel: OrdersModel? = null,
        orderModel: OrdersModel = OrdersModel(),
        @Payload c : UserModel, @DestinationVariable("uuid") customerUUID : String){

        //ищем пользователя в таблице
        val customer = userService.getByPhoneNumber(c.phone)

        //val customer = userService.getByUserUUID(customerUUID)
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
                if (foundDriver != null) {
                    //обновляем информацию о заказе
                    order.driverID = foundDriver.driverID
                    order = ordersService.updateInfo(order)
                    //отправляем найденному водителю предложение о заказе
                    logger.info(foundDriver.driver.uuid)
                    simpMessagingTemplate.convertAndSend(
                        "/topic/${foundDriver.driver.uuid}",
                        ResponseModel(MessageType.ORDER_REQUEST, order)
                    )
                } else {
                    simpMessagingTemplate.convertAndSend(
                        "/topic/${customer.uuid}", ResponseModel(MessageType.NO_ORDERS, order)
                    )
                }
            }
        }


    }

    @MessageMapping("/order.accept.{uuid}") //водитель принял заказ
    fun acceptOrder(@Payload orderModel: OrdersModel, @DestinationVariable("uuid") driverUUID : String){
        val order = ordersService.getByOrderUUID(orderModel.uuid)
        if (order != null) {
            simpMessagingTemplate.convertAndSend(
                "/topic/${orderModel.customer!!.uuid}",
                ResponseModel(MessageType.ORDER_ACCEPT, order)
            )
            simpMessagingTemplate.convertAndSend(
                "/topic/${orderModel.driver!!.uuid}",
                ResponseModel(MessageType.ORDER_ACCEPT, order)
            )
        }
    }

    @MessageMapping("/order.reject.{uuid}") //водитель отказался от заказа
    fun rejectOrder(@Payload orderModel: OrdersModel, @DestinationVariable("uuid") driverUUID : String){
        val order = ordersService.getByOrderUUID(orderModel.uuid)
        if (order != null) {
            rejectedOrdersService.reject(RejectedOrdersModel(driverID = order.driverID, orderUuid = order.uuid))
            order.isNew = false
            ordersService.updateInfo(order)
            makeOrder(order, order.customer!!, order.customer!!.uuid)
        }
    }

    @MessageMapping("/order.finish.{uuid}") //водитель отказался от заказа
    fun finishOrder(@Payload orderModel: OrdersModel, @DestinationVariable("uuid") driverUUID : String){
        val order = ordersService.getByOrderUUID(orderModel.uuid)
        if (order != null) {
            order.isFinished = true
            ordersService.updateInfo(order)
            
            simpMessagingTemplate.convertAndSend(
                "/topic/${orderModel.driver!!.uuid}",
                ResponseModel(MessageType.ORDER_FINISHED, order)
            )
            simpMessagingTemplate.convertAndSend(
                "/topic/${orderModel.customer!!.uuid}",
                ResponseModel(MessageType.ORDER_FINISHED, order)
            )
        }
    }

    private fun getNearestDriver(customer : UserModel, orderUUID: String) : DriverAvailable? {
        //получаем список доступных водителей
        val availableDrivers = driverAvailableService.getAll()
        logger.info(availableDrivers)
        //получаем список водителей, которые отказались от выполнения заказа
        val rejectedOrders = rejectedOrdersService.getByOrderUUID(orderUUID)
        //подходящие водители
        val map : HashMap<Double, DriverAvailable> = hashMapOf()

        for (driverAv in availableDrivers){
            val driver = driverAv.driver
            //проверяем, отказал ли водитель в выполнении заказа
            var wasFoundRejected = false
            for (order in rejectedOrders){
                if (driverAv.id == order.driverID) wasFoundRejected = true
            }
            //если не отказал, то проверяем расстояние между клиентом и водителем
            if (!wasFoundRejected){
                //рассчитываем дистанцию между клиентом и водителем
                val distance = calcDistance(customer.latitude, customer.longitude, driver.latitude, driver.longitude)
                if (distance <= driverAv.rideDistance) map[distance] = driverAv
                logger.info(distance)
            }

        }
        logger.info(map)

        //ищем подходящего водителя, сортируя сначала по дистанции, а потом по цене за минуту
        val comparator = compareBy<Pair<Double, DriverAvailable>>{it.first}
            .thenComparator { a: Pair<Double, DriverAvailable>, b: Pair<Double, DriverAvailable> ->
                compareValues(
                    a.first,
                    b.second.pricePerMinute
                )
            }
        //получаем первое значение - это и есть подходящий водитель
        val list = map.toList().sortedWith(comparator)
        logger.info(list)
        return if (list.isNotEmpty())
            list[0].second
        else {
            null
        }
    }

    private fun calcDistance(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double): Double {
        val theta = lon1 - lon2
        var dist =
            sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(
                deg2rad(theta)
            )
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 1.609344 * 1000
        return dist // in meters
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