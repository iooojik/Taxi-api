package octii.dev.taxi.repositories

import octii.dev.taxi.models.DriverAvailable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DriverAvailableRepository : JpaRepository<DriverAvailable, Long> {
}