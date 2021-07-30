package octii.dev.taxi.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "orders")
data class OrdersModel(
    @Id
    @GeneratedValue
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "driver_id")
    var driverID : Long = (-1).toLong(),
    @Column(name = "customer_id")
    var customerID : Long = (-1).toLong(),
    @Column(name = "uuid")
    var uuid : String = UUID.randomUUID().toString(),
    @Column(name = "is_finished")
    var isFinished : Boolean = false,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "_id", referencedColumnName = "driver_id")
    var driver : UserModel? = null,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "_id", referencedColumnName = "customer_id")
    var customer : UserModel,
)
