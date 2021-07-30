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

    fun updateInfo(orderModel: OrdersModel) : OrdersModel = ordersRepository.save(orderModel)

    fun getByOrderUUID(uuid : String) : OrdersModel? = ordersRepository.getByUuid(uuid)
}