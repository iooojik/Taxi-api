package octii.dev.taxi.contollers

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.services.OrdersService
import octii.dev.taxi.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

}