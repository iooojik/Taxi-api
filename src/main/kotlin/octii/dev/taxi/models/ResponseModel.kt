package octii.dev.taxi.models

import octii.dev.taxi.models.database.CoordinatesModel
import octii.dev.taxi.models.database.OrdersModel
import octii.dev.taxi.models.database.TaximeterModel

data class ResponseModel(
    var type: MessageType? = null,
    var order : OrdersModel? = null,
    var taximeterModel: TaximeterModel? = null,
    var coordinates : CoordinatesModel? = null
)