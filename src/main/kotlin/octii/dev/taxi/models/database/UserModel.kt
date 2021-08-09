package octii.dev.taxi.models.database

import com.fasterxml.jackson.annotation.JsonIgnore
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
    var userName : String = "",
    @Column(name = "user_phone")
    var phone : String = "",
    @Column(name = "user_type")
    var type : String = "client",
    @Column(name = "token")
    var token : String = "",
    @Column(name = "is_whatsapp")
    var isWhatsapp : Boolean = false,
    @Column(name = "is_viber")
    var isViber : Boolean = false,
    @Column(name = "uuid")
    var uuid : String = UUID.randomUUID().toString(),
    @Column(name = "is_only_client")
    var isOnlyClient : Boolean = type == "client",
    @Column(name = "avatar_url")
    var avatarURL : String? = "",
    @Column(name = "last_login")
    @JsonIgnore
    var lastLogin : String? = Date().toString(),
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var languages : List<SpeakingLanguagesModel>? = listOf(),
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var coordinates : CoordinatesModel? = null,
    @OneToOne(mappedBy = "driver", cascade = [CascadeType.ALL])
    var driver : DriverModel? = null,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var files : List<UsersToFiles>? = listOf(),
)
