package octii.dev.taxi.models

data class ResponseModel(
    var type: MessageType? = null,
    var order : OrdersModel? = null,
    var taximeterModel: TaximeterModel? = null,
    var coordinates : CoordinatesModel? = null
)