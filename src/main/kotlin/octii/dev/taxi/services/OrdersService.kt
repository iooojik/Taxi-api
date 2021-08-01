package octii.dev.taxi.services

import octii.dev.taxi.models.OrdersModel
import octii.dev.taxi.repositories.OrdersRepository
import org.springframework.stereotype.Service

@Service
class OrdersService(val ordersRepository: OrdersRepository) {

    fun registerNewOrder(orderModel : OrdersModel) : OrdersModel{
        return ordersRepository.save(orderModel)
    }

    fun getAll() : List<OrdersModel> = ordersRepository.findAll()

    fun update(orderModel: OrdersModel) : OrdersModel = ordersRepository.save(orderModel)

    fun getByOrderUUID(uuid : String) : OrdersModel? = ordersRepository.getByUuid(uuid)

    fun getByDriverID(id : Long) : OrdersModel? = ordersRepository.getAllByDriverID(id).last()

    fun getByCustomerID(id : Long) : OrdersModel? {
        val list = ordersRepository.getAllByCustomerID(id)
        return if (list.isEmpty()) null
        else list.last()
    }
}