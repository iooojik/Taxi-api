package octii.dev.taxi.models.database

import org.hibernate.Hibernate
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
    @Column(name = "is_accepted")
    var isAccepted : Boolean? = false,
    @Column(name = "date_created")
    var dateCreated : String? = Date().toString(),
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "driver_id", referencedColumnName = "_id")
    var driver : UserModel? = null,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "customer_id", referencedColumnName = "_id")
    var customer : UserModel? = null,
    var isNew : Boolean = true
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as OrdersModel

        return id == other.id
    }

    override fun hashCode(): Int = 1904319463

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , driverID = $driverID , customerID = $customerID , uuid = $uuid , isFinished = $isFinished , driver = $driver , customer = $customer , isNew = $isNew )"
    }
}
