package octii.dev.taxi.contollers.rest

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.models.database.OrdersModel
import octii.dev.taxi.models.database.UserModel
import octii.dev.taxi.services.OrdersService
import octii.dev.taxi.services.RejectedOrdersService
import octii.dev.taxi.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Controller
@RequestMapping("/orders/")
class OrdersController(
	private val ordersService: OrdersService,
	private val rejectedOrdersService: RejectedOrdersService, private val userService: UserService
) : ResponseGenerator {
	
	@PostMapping("/check")
	fun ordersCheck(@RequestBody userModel: UserModel): ResponseEntity<Any> {
		return if (userModel.id < 0) errorResponse()
		else {
			//получаем последний заказ по id водителя
			var order = ordersService.getOrderByDriverID(userModel.id)
			//получаем последний заказ по id клиента
			if (order.id < 1) order = ordersService.getOrderByCustomerID(userModel.id)
			if (order.driverID != null) {
				val foundDriver = userService.getById(order.driverID!!)
				//проверяем, был ли отклонён заказ
				if (foundDriver != null) {
					if (order.driverID!! > 0 && order.customerID > 0 && !isReject(order, foundDriver.uuid))
						okResponse(order)
					else errorResponse()
				} else errorResponse()
			} else okResponse(OrdersModel())
		}
	}
	
	private fun isReject(order: OrdersModel, driverUUID: String): Boolean {
		val rejectedList = rejectedOrdersService.getByOrderUUID(order.uuid)
		var found = false
		if (rejectedList.isNotEmpty()) {
			rejectedList.forEach {
				if (it.driverUUID == driverUUID) found = true
			}
		}
		return found
	}
	
}