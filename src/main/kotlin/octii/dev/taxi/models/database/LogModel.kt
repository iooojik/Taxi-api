package octii.dev.taxi.models.database

import javax.persistence.*

@Entity
@Table(name = "logs")
data class LogModel (
	@Id
	@GeneratedValue
	@Column(name = "_id")
	var id: Long = -1,
	@Column(name = "user_uuid")
	var userUUID : String,
	@Column(name = "path")
	var path : String
)