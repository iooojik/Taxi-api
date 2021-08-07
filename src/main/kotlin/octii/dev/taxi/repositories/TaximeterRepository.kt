package octii.dev.taxi.repositories

import octii.dev.taxi.models.database.TaximeterModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaximeterRepository : JpaRepository<TaximeterModel, Long> {

}