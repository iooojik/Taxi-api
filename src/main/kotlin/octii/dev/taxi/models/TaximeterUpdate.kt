package octii.dev.taxi.models

data class TaximeterUpdate(
    var coordinates : CoordinatesModel? = null,
    var recipientUUID : String? = null
)