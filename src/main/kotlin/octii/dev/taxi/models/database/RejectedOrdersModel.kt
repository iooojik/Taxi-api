package octii.dev.taxi.models.database

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "rejected_orders")
data class RejectedOrdersModel(
    @Id
    @GeneratedValue
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "driver_uuid")
    var driverUUID : String = UUID.randomUUID().toString(),
    @Column(name = "order_uuid")
    var orderUuid : String
)