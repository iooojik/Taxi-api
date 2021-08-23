package octii.dev.taxi.models.database

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "logs")
data class LogModel(
	@Id
	@GeneratedValue
	@JsonIgnore
	@Column(name = "_id")
	var id: Long = -1,
	@Column(name = "user_uuid")
	var userUUID: String,
	@Column(name = "path")
	var path: String
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as LogModel
		
		return id == other.id
	}
	
	override fun hashCode(): Int = 1528166952
	
	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id , userUUID = $userUUID , path = $path )"
	}
}