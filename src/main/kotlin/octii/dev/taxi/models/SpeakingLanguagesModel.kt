package octii.dev.taxi.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "speaking_languages")
data class SpeakingLanguagesModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "driver_id")
    var driverId : Long = -1,
    @Column(name = "language")
    var language : String = "ru"
)
