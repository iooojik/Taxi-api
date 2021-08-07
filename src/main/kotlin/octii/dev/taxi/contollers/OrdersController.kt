package octii.dev.taxi.contollers

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.models.database.OrdersModel
import octii.dev.taxi.models.database.UserModel
import octii.dev.taxi.services.OrdersService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@Controller
@RequestMapping("/orders/")
class OrdersController(private val ordersService: OrdersService) : ResponseGenerator {

    @GetMapping("/")
    fun getOrders() : ResponseEntity<Any> = okResponse(ordersService.getAll())

    @GetMapping("/completed")
    fun getCompletedOrders() : ResponseEntity<Any> = okResponse(ordersService.getAllCompleted())

    @GetMapping("/not.completed")
    fun getNotCompletedOrders() : ResponseEntity<Any> = okResponse(ordersService.getAllNotCompleted())

    @PostMapping("/check")
    fun ordersCheck(@RequestBody userModel: UserModel) : ResponseEntity<Any> {
        return if (userModel.id < 0) errorResponse()
        else{
            var order = ordersService.getOrderByDriverID(userModel.id)
            if (order.id < 1) order = ordersService.getOrderByCustomerID(userModel.id)
            if (order.driverID != null)
                okResponse(order)
            else okResponse(OrdersModel())
        }
    }

}