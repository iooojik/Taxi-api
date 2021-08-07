package octii.dev.taxi.repositories

import octii.dev.taxi.models.database.DriverModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DriverRepository : JpaRepository<DriverModel, Long> {
    @Suppress("SpringDataMethodInconsistencyInspection")
    fun findAllByIsWorking(working : Boolean = true) : List<DriverModel>

    fun findByDriver_Id(id : Long) : DriverModel?

}