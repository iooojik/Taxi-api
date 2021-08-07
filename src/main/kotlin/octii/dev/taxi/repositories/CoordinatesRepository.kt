package octii.dev.taxi.repositories

import octii.dev.taxi.models.database.CoordinatesModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CoordinatesRepository : JpaRepository<CoordinatesModel, Long> {

    fun findByUserId(id : Long) : CoordinatesModel?

}