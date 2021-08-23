package octii.dev.taxi.models.database

import org.hibernate.Hibernate
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
	var driverUUID: String = UUID.randomUUID().toString(),
	@Column(name = "order_uuid")
	var orderUuid: String
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as RejectedOrdersModel
		
		return id == other.id
	}
	
	override fun hashCode(): Int = 1187407296
	
	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id , driverUUID = $driverUUID , orderUuid = $orderUuid )"
	}
	
}