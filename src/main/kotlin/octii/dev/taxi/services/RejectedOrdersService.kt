package octii.dev.taxi.services

import octii.dev.taxi.models.RejectedOrdersModel
import octii.dev.taxi.repositories.OrdersRepository
import octii.dev.taxi.repositories.RejectedOrdersRepository
import org.springframework.stereotype.Service

@Service
class RejectedOrdersService(val ordersRepository: OrdersRepository,
                            val rejectedOrdersRepository: RejectedOrdersRepository) {

    fun getAll() : List<RejectedOrdersModel> = rejectedOrdersRepository.findAll()

    fun getByOrderUUID(uuid : String) : List<RejectedOrdersModel> = rejectedOrdersRepository.findAllByOrderUuid(uuid)

    fun reject(rejectedOrderModel: RejectedOrdersModel) : RejectedOrdersModel = rejectedOrdersRepository.save(rejectedOrderModel)
}