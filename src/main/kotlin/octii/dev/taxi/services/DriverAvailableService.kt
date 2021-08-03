package octii.dev.taxi.services

import octii.dev.taxi.models.DriverAvailableModel
import octii.dev.taxi.models.UserModel
import octii.dev.taxi.repositories.DriverAvailableRepository
import org.springframework.stereotype.Service

@Service
class DriverAvailableService(val driverAvailableRepository: DriverAvailableRepository) {
    fun getAll() : List<DriverAvailableModel> = driverAvailableRepository.findAllByIsWorking()

    fun getByDriverID(id : Long) : DriverAvailableModel? = driverAvailableRepository.findByDriver_Id(id)

    fun save(driverAvailableModel: DriverAvailableModel) : DriverAvailableModel? = driverAvailableRepository.save(driverAvailableModel)
}