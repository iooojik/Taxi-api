package octii.dev.taxi.models

import octii.dev.taxi.constants.OrderActions
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "taximeter")
data class TaximeterModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,
    @Column(name = "timestamp")
    var timeStamp : String? = Date().toString(),
    @Column(name = "action")
    var action : String = OrderActions.ACTION_NO,
    @Column(name = "coordinates_id", insertable = false, updatable = false)
    var coordinatesId : Long? = (-1).toLong(),
    @Column(name = "prices_id", insertable = false, updatable = false)
    var pricesId : Long? = (-1).toLong(),
    @Column(name = "order_id", insertable = false, updatable = false)
    var orderId : Long? = (-1).toLong(),
    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "coordinates_id", referencedColumnName = "_id")
    var coordinates : CoordinatesModel = CoordinatesModel(),
    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "prices_id", referencedColumnName = "_id")
    var prices : Prices = Prices(),
    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "order_id", referencedColumnName = "_id")
    var orderModel: OrdersModel = OrdersModel()
)