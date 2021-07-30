package octii.dev.taxi.models

data class ResponseModel(
    var type: MessageType? = null,
    var body : Any? = null
)