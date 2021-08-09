package octii.dev.taxi.models.database

import com.fasterxml.jackson.annotation.JsonBackReference
import octii.dev.taxi.constants.TaximeterStatus
import octii.dev.taxi.models.Prices
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
    var action : String = TaximeterStatus.ACTION_NO,
    @Column(name = "order_uuid")
    var orderUUID : String = ""
)