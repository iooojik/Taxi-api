package octii.dev.taxi.models.sockets

import octii.dev.taxi.constants.MessageType
import octii.dev.taxi.constants.TaximeterType
import octii.dev.taxi.models.database.CoordinatesModel
import octii.dev.taxi.models.database.OrdersModel
import octii.dev.taxi.models.database.TaximeterModel

data class OrdersResponseModel(
    var type: MessageType? = null,
    var order : OrdersModel? = null,
    var taximeterModel: TaximeterModel? = null,
    var coordinates : CoordinatesModel? = null
)