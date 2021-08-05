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
    var timeStamp : String = Date().toString(),
    @Column(name = "action")
    var action : String = OrderActions.ACTION_NO,
    @OneToOne
    var coordinates : CoordinatesModel = CoordinatesModel(),
    @OneToOne
    var prices : Prices = Prices()
)