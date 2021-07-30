package octii.dev.taxi.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
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
    var uuid : String = UUID.randomUUID().toString()
)
