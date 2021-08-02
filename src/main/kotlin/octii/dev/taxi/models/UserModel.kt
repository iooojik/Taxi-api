package octii.dev.taxi.models

import com.fasterxml.jackson.annotation.JsonManagedReference
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
    var token : String = UUID.randomUUID().toString(),
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
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "user_id")
    var languages : List<SpeakingLanguagesModel> = listOf(),
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JsonManagedReference
    var coordinates : CoordinatesModel? = null
)
