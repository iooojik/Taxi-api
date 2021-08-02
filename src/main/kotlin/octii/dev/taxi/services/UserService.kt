package octii.dev.taxi.services

import octii.dev.taxi.models.CoordinatesModel
import octii.dev.taxi.models.DriverAvailableModel
import octii.dev.taxi.models.UserModel
import octii.dev.taxi.repositories.CoordinatesRepository
import octii.dev.taxi.repositories.DriverAvailableRepository
import octii.dev.taxi.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository,
                  val driverAvailableRepository: DriverAvailableRepository,
                  val languageService: LanguageService, val coordinatesRepository: CoordinatesRepository) {

    fun getAllUsers(): List<UserModel> = userRepository.findAll()

    fun registerUser(user : UserModel) : UserModel {
        //сохраняем пользователя и получаем его стандартные данные
        //сохраняем модель с координатами
        var savedUser = userRepository.save(user)
        savedUser.coordinates = coordinatesRepository.save(CoordinatesModel(user = savedUser))
        if (savedUser.type == "driver")
            driverAvailableRepository.save(DriverAvailableModel(driver = user, driverID = user.id))
        //обновляем данные пользователя
        savedUser = userRepository.save(savedUser)
        return savedUser
    }

    fun loginWithToken(user: UserModel) : UserModel?{
        val foundUser = userRepository.findByToken(user.token)
        if (foundUser != null){
            foundUser.token = UUID.randomUUID().toString()
            userRepository.save(foundUser)
        }
        return null
    }

    fun login(user: UserModel): UserModel {
        //находим пользователя по номеру телефона
        val foundUser = userRepository.findByPhone(user.phone)
        if (foundUser != null){
            if (foundUser.type == "driver"){
                val foundDriverAvailableModel = driverAvailableRepository.findByDriver(foundUser)
                if (foundDriverAvailableModel == null){
                    driverAvailableRepository.save(DriverAvailableModel(driver = foundUser, driverID = foundUser.id))
                }
            }
        }
        return if (foundUser != null){
            foundUser.isWhatsapp = user.isWhatsapp
            foundUser.isViber = user.isViber
            foundUser.token = UUID.randomUUID().toString()
            if (user.coordinates != null)
                foundUser.coordinates = user.coordinates
            userRepository.save(foundUser)
        } else {
            registerUser(user)
        }
    }

    fun getByPhoneNumber(phone : String) : UserModel? = userRepository.findByPhone(phone)

    fun getByUserUUID(uuid : String) : UserModel? = userRepository.findByUuid(uuid)

    fun update(user : UserModel) : UserModel {
        return userRepository.save(user)
    }
}