package octii.dev.taxi.models

data class TokenAuthorization(
    val user : UserModel,
    val order : OrdersModel? = null
)
