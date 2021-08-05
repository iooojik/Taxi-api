package octii.dev.taxi.models

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
@Table(name = "speaking_languages")
class SpeakingLanguagesModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "language")
    var language : String = "sr",
    @Column(name = "user_id", insertable = false, updatable = false)
    var userId : Long = -1,
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    var user : UserModel? = null
) {
    override fun toString(): String {
        return "{\"language\": ${this.language}}"
    }
}
