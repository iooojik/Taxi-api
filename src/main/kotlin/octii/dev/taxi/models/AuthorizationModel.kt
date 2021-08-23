package octii.dev.taxi.models

import octii.dev.taxi.models.database.OrdersModel
import octii.dev.taxi.models.database.UserModel

data class AuthorizationModel(
	val user: UserModel? = null,
	val lastOrder: OrdersModel? = null
)