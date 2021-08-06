package octii.dev.taxi.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*


@Entity
@Table(name = "prices")
class Prices(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "price_per_minute")
    var pricePerMinute : Float = 1f,
    @Column(name = "price_per_km")
    var pricePerKm : Float = 10f,
    @Column(name = "price_waiting_min")
    var priceWaitingMin : Float = 1f,
    @OneToOne(optional = false)
    @JoinColumn(name = "driver_id")
    @JsonIgnore
    var driver : DriverModel? = null,
){
    override fun toString(): String {
        return "{\"id\": ${this.id}," +
                "\"pricePerMinute\": ${this.pricePerMinute}," +
                "\"pricePerKm\": ${this.pricePerKm}," +
                "\"priceWaitingMin\": ${this.priceWaitingMin} }"
    }
}