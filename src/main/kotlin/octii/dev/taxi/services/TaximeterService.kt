package octii.dev.taxi.services

import octii.dev.taxi.models.database.TaximeterModel
import octii.dev.taxi.repositories.TaximeterRepository
import org.springframework.stereotype.Service

@Service
class TaximeterService(val userService: UserService, val ordersService: OrdersService,
                       val taximeterRepository: TaximeterRepository) {

    fun getAll() : List<TaximeterModel> = taximeterRepository.findAll()

    fun save(taximeterModel: TaximeterModel) : TaximeterModel = taximeterRepository.save(taximeterModel)

}