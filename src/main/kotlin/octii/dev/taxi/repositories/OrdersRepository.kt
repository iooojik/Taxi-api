package octii.dev.taxi.repositories

import octii.dev.taxi.models.database.OrdersModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrdersRepository: JpaRepository<OrdersModel, Long> {
    fun getByUuid(uuid : String) : OrdersModel?

    fun getAllByDriverID(driverID: Long) : List<OrdersModel>

    fun getAllByCustomerID(customerID: Long) : List<OrdersModel>

    fun findAllByIsFinished(isFinished : Boolean) : List<OrdersModel>
}