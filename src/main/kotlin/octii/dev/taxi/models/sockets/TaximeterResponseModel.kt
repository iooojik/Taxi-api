package octii.dev.taxi.models.sockets

import octii.dev.taxi.constants.MessageType
import octii.dev.taxi.constants.TaximeterStatus
import octii.dev.taxi.constants.TaximeterType
import octii.dev.taxi.models.database.CoordinatesModel
import octii.dev.taxi.models.database.OrdersModel
import octii.dev.taxi.models.database.TaximeterModel

data class TaximeterResponseModel(
    var type: TaximeterType? = null,
    var coordinates : CoordinatesModel? = null,
    var status : String = TaximeterStatus.ACTION_NO,
    var isWaiting : Boolean = false
)
