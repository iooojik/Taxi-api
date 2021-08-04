package octii.dev.taxi.models

data class AuthorizationModel(
    val user : UserModel? = null,
    val lastOrder : OrdersModel? = null
)