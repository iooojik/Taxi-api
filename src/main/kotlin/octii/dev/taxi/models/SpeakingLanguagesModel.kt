package octii.dev.taxi.models

import javax.persistence.*

@Entity
@Table(name = "speaking_languages")
data class SpeakingLanguagesModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "user_id")
    var userId : Long = -1,
    @Column(name = "language")
    var language : String = "ru"
)
