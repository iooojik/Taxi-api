package octii.dev.taxi.models.database

import octii.dev.taxi.constants.TaximeterStatus
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "taximeter")
data class TaximeterModel(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "_id")
	var id: Long = -1,
	@Column(name = "timestamp")
	var timeStamp: String? = Date().toString(),
	@Column(name = "action")
	var action: String = TaximeterStatus.ACTION_NO,
	@Column(name = "order_uuid")
	var orderUUID: String = ""
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as TaximeterModel
		
		return id != null && id == other.id
	}
	
	override fun hashCode(): Int = 866106677
	
	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id , timeStamp = $timeStamp , action = $action , orderUUID = $orderUUID )"
	}
}