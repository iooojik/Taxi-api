package octii.dev.taxi.services

import octii.dev.taxi.models.CoordinatesModel
import octii.dev.taxi.repositories.CoordinatesRepository
import org.springframework.stereotype.Service

@Service
class CoordinateService(private val coordinatesRepository: CoordinatesRepository) {
    fun update(coordinatesModel: CoordinatesModel, userId : Long) : CoordinatesModel? {
        val foundCoordinates = coordinatesRepository.findByUserId(userId)
        return if (foundCoordinates != null){
            foundCoordinates.latitude = coordinatesModel.latitude
            foundCoordinates.longitude = coordinatesModel.longitude
            coordinatesRepository.save(foundCoordinates)
        } else{
            coordinatesModel.userId = userId
            coordinatesRepository.save(coordinatesModel)
        }
    }
}