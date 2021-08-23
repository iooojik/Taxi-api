package octii.dev.taxi.services

import octii.dev.taxi.models.database.OrdersModel
import octii.dev.taxi.repositories.OrdersRepository
import org.springframework.stereotype.Service

@Service
class OrdersService(val ordersRepository: OrdersRepository) {
	
	fun save(orderModel: OrdersModel): OrdersModel = ordersRepository.save(orderModel)
	
	fun registerNewOrder(orderModel: OrdersModel): OrdersModel {
		return ordersRepository.save(orderModel)
	}
	
	fun getAll(): List<OrdersModel> = ordersRepository.findAll()
	
	fun getAllNotCompleted(): List<OrdersModel> = ordersRepository.findAllByIsFinished(false)
	
	fun getAllCompleted(): List<OrdersModel> = ordersRepository.findAllByIsFinished(true)
	
	fun update(orderModel: OrdersModel): OrdersModel = ordersRepository.save(orderModel)
	
	fun getByOrderUUID(uuid: String): OrdersModel? = ordersRepository.getByUuid(uuid)
	
	
	fun getOrderByDriverID(id: Long): OrdersModel {
		val orders = ordersRepository.getAllByDriverID(id)
		return if (orders.isNotEmpty()) orders.last()
		else OrdersModel()
	}
	
	fun getOrderByCustomerID(id: Long): OrdersModel {
		val list = ordersRepository.getAllByCustomerID(id)
		return if (list.isEmpty()) OrdersModel()
		else list.last()
	}
}