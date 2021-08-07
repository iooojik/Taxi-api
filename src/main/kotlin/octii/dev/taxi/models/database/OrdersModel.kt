package octii.dev.taxi.models.database

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "orders")
data class OrdersModel(
    @Id
    @GeneratedValue
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "driver_id", insertable = false, updatable = false)
    var driverID : Long? = (-1).toLong(),
    @Column(name = "customer_id", insertable = false, updatable = false)
    var customerID : Long = (-1).toLong(),
    @Column(name = "uuid")
    var uuid : String = UUID.randomUUID().toString(),
    @Column(name = "is_finished")
    var isFinished : Boolean = false,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "driver_id", referencedColumnName = "_id")
    var driver : UserModel? = null,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "customer_id", referencedColumnName = "_id")
    var customer : UserModel? = null,
    var isNew : Boolean = true
)
