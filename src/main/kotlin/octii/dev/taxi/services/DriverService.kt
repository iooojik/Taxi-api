package octii.dev.taxi.services

import octii.dev.taxi.models.DriverModel
import octii.dev.taxi.repositories.DriverRepository
import org.springframework.stereotype.Service

@Service
class DriverService(val driverRepository: DriverRepository) {
    fun getAll() : List<DriverModel> = driverRepository.findAllByIsWorking()

    fun getByDriverID(id : Long) : DriverModel? = driverRepository.findByDriver_Id(id)

    fun save(driverModel: DriverModel) : DriverModel? = driverRepository.save(driverModel)

    fun update(oldDriver: DriverModel, newDriver: DriverModel): DriverModel? {
        oldDriver.isWorking = newDriver.isWorking
        oldDriver.prices?.pricePerKm = if (newDriver.prices?.pricePerKm != null) newDriver.prices?.pricePerKm!! else 10f
        oldDriver.prices?.pricePerMinute =
            if (newDriver.prices?.pricePerMinute != null) newDriver.prices?.pricePerMinute!! else 1f
        oldDriver.prices?.priceWaitingMin =
            if (newDriver.prices?.priceWaitingMin != null) newDriver.prices?.priceWaitingMin!! else 1f

        return save(oldDriver)
    }
}