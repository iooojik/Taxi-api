package octii.dev.taxi.repositories

import octii.dev.taxi.models.DriverAvailableModel
import octii.dev.taxi.models.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DriverAvailableRepository : JpaRepository<DriverAvailableModel, Long> {
    @Suppress("SpringDataMethodInconsistencyInspection")
    fun findAllByIsWorking(working : Boolean = false) : List<DriverAvailableModel>

    fun findByDriver(driver : UserModel) : DriverAvailableModel?

    fun findByDriverID(id : Long) : DriverAvailableModel?
}