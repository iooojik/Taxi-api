package octii.dev.taxi.repositories

import octii.dev.taxi.models.Prices
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PricesRepository : JpaRepository<Prices, Long>