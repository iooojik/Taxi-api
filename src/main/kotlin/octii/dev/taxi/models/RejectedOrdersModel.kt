package octii.dev.taxi.models

import javax.persistence.*

@Entity
@Table(name = "rejected_orders")
data class RejectedOrdersModel(
    @Id
    @GeneratedValue
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "driver_id")
    var driverID : Long = (-1).toLong(),
    @Column(name = "order_uuid")
    var orderUuid : String
)