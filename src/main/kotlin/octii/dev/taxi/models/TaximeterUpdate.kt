package octii.dev.taxi.models

import octii.dev.taxi.models.database.CoordinatesModel

data class TaximeterUpdate(
	var coordinates: CoordinatesModel? = null,
	var recipientUUID: String? = null,
	var orderUUID: String
)