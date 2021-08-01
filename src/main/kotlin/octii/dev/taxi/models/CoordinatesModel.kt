package octii.dev.taxi.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "coordinates")
data class CoordinatesModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "user_id")
    var userId : Long = -1,
    @Column(name = "latitude")
    var latitude : Double = 0.0,
    @Column(name = "longitude")
    var longitude : Double = 0.0,
)
