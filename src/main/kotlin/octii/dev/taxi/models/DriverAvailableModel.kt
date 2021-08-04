package octii.dev.taxi.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "drivers_available")
class DriverAvailableModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "ride_distance")
    var rideDistance : Float = 15f,
    @Column(name = "price_per_minute")
    var pricePerMinute : Float = 1f,
    @Column(name = "price_per_km")
    var pricePerKm : Float = 10f,
    @Column(name = "price_waiting_min")
    var priceWaitingMin : Float = 1f,
    @Column(name = "is_working")
    var isWorking : Boolean = false,
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var driver : UserModel? = null
) {
    override fun toString(): String {
        return "{\"id\": ${this.id}," +
                "\"rideDistance\": ${this.rideDistance}," +
                "\"pricePerMinute\": ${this.pricePerMinute}," +
                "\"pricePerKm\": ${this.pricePerKm}," +
                "\"priceWaitingMin\": ${this.priceWaitingMin}," +
                "\"isWorking\": ${this.isWorking} }"
    }
}
