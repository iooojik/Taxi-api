package octii.dev.taxi.models.database

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class UserModel(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "_id", insertable = false, updatable = false)
	var id: Long = -1,
	@Column(name = "user_name")
	var userName: String = "",
	@Column(name = "user_phone")
	var phone: String = "",
	@Column(name = "user_type")
	var type: String = "client",
	@Column(name = "token")
	var token: String = "",
	@Column(name = "is_whatsapp")
	var isWhatsapp: Boolean = false,
	@Column(name = "is_viber")
	var isViber: Boolean = false,
	@Column(name = "uuid")
	var uuid: String = UUID.randomUUID().toString(),
	@Column(name = "is_only_client")
	var isOnlyClient: Boolean = type == "client",
	@Column(name = "avatar_url")
	var avatarURL: String? = "",
	@Column(name = "is_busy")
	var isBusy: Boolean? = false,
	@Column(name = "last_login")
	@JsonIgnore
	var lastLogin: String? = Date().toString(),
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.PERSIST])
	var languages: List<SpeakingLanguagesModel>? = listOf(),
	@OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
	var coordinates: CoordinatesModel? = null,
	@OneToOne(mappedBy = "driver", cascade = [CascadeType.ALL])
	var driver: DriverModel? = null,
	@OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
	var files: List<UsersToFiles>? = listOf(),
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
		other as UserModel
		
		return id == other.id
	}
	
	override fun hashCode(): Int = 484641579
	
	@Override
	override fun toString(): String {
		return this::class.simpleName + "(id = $id , userName = $userName , phone = $phone , type = $type , token = $token , isWhatsapp = $isWhatsapp , isViber = $isViber , uuid = $uuid , isOnlyClient = $isOnlyClient , avatarURL = $avatarURL , isBusy = $isBusy , lastLogin = $lastLogin , coordinates = $coordinates , driver = $driver )"
	}
}
