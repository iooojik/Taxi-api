package octii.dev.taxi.services

import octii.dev.taxi.models.DriverAvailable
import octii.dev.taxi.repositories.DriverAvailableRepository
import org.springframework.stereotype.Service

@Service
class DriverAvailableService(val driverAvailableRepository: DriverAvailableRepository) {
    fun getAll() : List<DriverAvailable> = driverAvailableRepository.findAll()
}