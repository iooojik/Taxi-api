package octii.dev.taxi.models

data class TokenAuthorization(
    val user : UserModel? = null,
    val order : OrdersModel? = null
)
