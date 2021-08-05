package octii.dev.taxi.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "drivers")
class DriverModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "is_working")
    var isWorking : Boolean = false,
    @Column(name = "ride_distance")
    var rideDistance : Float = 15f,
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var driver : UserModel? = null,
    @OneToOne(mappedBy = "driver", cascade = [CascadeType.ALL])
    var prices: Prices? = null
) {
    override fun toString(): String {
        return "{\"id\": ${this.id}," +
                "\"prices\": ${this.prices}," +
                "\"rideDistance\": ${this.rideDistance}," +
                "\"isWorking\": ${this.isWorking} }"
    }
}
