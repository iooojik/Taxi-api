package octii.dev.taxi.services

import octii.dev.taxi.models.Prices
import octii.dev.taxi.repositories.PricesRepository
import org.springframework.stereotype.Service

@Service
class PricesService(val pricesRepository: PricesRepository) {
    fun save(prices: Prices) : Prices = pricesRepository.save(prices)
}