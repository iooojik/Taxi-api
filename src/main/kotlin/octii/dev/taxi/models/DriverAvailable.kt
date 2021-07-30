package octii.dev.taxi.models

import javax.persistence.*

@Entity
@Table(name = "drivers_available")
data class DriverAvailable(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "driver_id")
    var driverID : Long = (-1).toLong(),
    @Column(name = "latitude")
    var latitude : String = "",
    @Column(name = "longitude")
    var longitude : String = "",
    @Column(name = "ride_distance")
    var rideDistance : Int = 15,
    @Column(name = "price_per_minute")
    var pricePerMinute : Int = 1,
    @Column(name = "price_per_km")
    var pricePerKm : Int = 10,
    @Column(name = "price_waiting_min")
    var priceWaitingMin : Int = 1,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "_id", referencedColumnName = "driver_id")
    var driver : UserModel? = null

)
