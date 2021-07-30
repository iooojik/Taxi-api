package octii.dev.taxi.models

import javax.persistence.*

@Entity
@Table(name = "drivers_available")
data class DriverAvailable(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "driver_id", insertable = false, updatable = false)
    var driverID : Long = (-1).toLong(),
    @Column(name = "ride_distance")
    var rideDistance : Int = 15,
    @Column(name = "price_per_minute")
    var pricePerMinute : Int = 1,
    @Column(name = "price_per_km")
    var pricePerKm : Int = 10,
    @Column(name = "price_waiting_min")
    var priceWaitingMin : Int = 1,
    @Column(name = "is_working")
    var isWorking : Boolean = false,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "driver_id", referencedColumnName = "_id")
    var driver : UserModel

)
