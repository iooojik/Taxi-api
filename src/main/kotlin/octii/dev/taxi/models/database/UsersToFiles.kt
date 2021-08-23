package octii.dev.taxi.models.database

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
@Table(name = "files")
class UsersToFiles(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "_id")
	var id: Long = -1,
	@Column(name = "url", updatable = false)
	var url: String = "",
	@Column(name = "file_name", updatable = false)
	var fileName: String = "",
	@Column(name = "file_extension", updatable = false)
	var fileExtension: String = "",
	@Column(name = "file_type", updatable = false)
	var type: String = "",
	@Column(name = "is_new")
	var isNew: Boolean = true,
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	var user: UserModel? = null
) {
	override fun toString(): String {
		return "{" +
				"                \"id\": $id," +
				"                \"url\": \"$url\"," +
				"                \"fileName\": \"$fileName\"," +
				"                \"fileExtension\": \"$fileExtension\"," +
				"                \"type\": \"$type\"," +
				"                \"isNew\": $isNew" +
				"            }"
	}
}