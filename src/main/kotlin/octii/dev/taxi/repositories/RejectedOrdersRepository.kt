package octii.dev.taxi.repositories

import octii.dev.taxi.models.OrdersModel
import octii.dev.taxi.models.RejectedOrdersModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RejectedOrdersRepository: JpaRepository<RejectedOrdersModel, Long> {
    fun findAllByOrderUuid(uuid : String) : List<RejectedOrdersModel>
}