package octii.dev.taxi.models.database

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
@Table(name = "coordinates")
class CoordinatesModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "latitude")
    var latitude : Double = 0.0,
    @Column(name = "longitude")
    var longitude : Double = 0.0,
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    var user : UserModel? = null
) {
    override fun toString(): String {
        return "{\"id\": ${this.id},\"latitude\": ${this.latitude}," +
                "\"longitude\": ${this.longitude}}"
    }
}
