package octii.dev.taxi.services

import octii.dev.taxi.models.CoordinatesModel
import octii.dev.taxi.models.DriverAvailableModel
import octii.dev.taxi.models.SpeakingLanguagesModel
import octii.dev.taxi.models.UserModel
import octii.dev.taxi.repositories.CoordinatesRepository
import octii.dev.taxi.repositories.DriverAvailableRepository
import octii.dev.taxi.repositories.LanguageRepository
import octii.dev.taxi.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository,
                  val driverAvailableRepository: DriverAvailableRepository,
                  val languageService: LanguageService,
                  val coordinatesRepository: CoordinatesRepository, val languageRepository: LanguageRepository) {

    fun getAllUsers(): List<UserModel> = userRepository.findAll()

    fun registerUser(user : UserModel) : UserModel? {
        return if (user.phone.isNotEmpty()) {
            //сохраняем пользователя и получаем его стандартные данные
            //сохраняем модель с координатами
            user.coordinates = null
            user.driver = null
            user.languages = null
            var savedUser = userRepository.save(user)
            savedUser.coordinates = coordinatesRepository.save(CoordinatesModel(user = savedUser))
            savedUser.languages = listOf(languageRepository.save(SpeakingLanguagesModel(user = savedUser)))
            savedUser.driver = driverAvailableRepository.save(DriverAvailableModel(driver = savedUser))
            savedUser.token = UUID.randomUUID().toString()
            savedUser.uuid = UUID.randomUUID().toString()
            //обновляем данные пользователя
            savedUser = userRepository.save(savedUser)
            savedUser
        } else null
    }

    fun login(user: UserModel): UserModel? {
        //находим пользователя по номеру телефона
        val foundUser = userRepository.findByPhone(user.phone)
        return if (foundUser != null){
            foundUser.isWhatsapp = user.isWhatsapp
            foundUser.isViber = user.isViber
            foundUser.token = UUID.randomUUID().toString()
            userRepository.save(foundUser)
        } else {
            registerUser(user)
        }
    }

    fun loginWithToken(user: UserModel) : UserModel?{
        val foundUser = userRepository.findByToken(user.token)
        return if (foundUser != null){
            foundUser.token = UUID.randomUUID().toString()
            userRepository.save(foundUser)
        } else null
    }


    fun getByPhoneNumber(phone : String) : UserModel? = userRepository.findByPhone(phone)

    fun getByUserUUID(uuid : String) : UserModel? = userRepository.findByUuid(uuid)

    fun update(user : UserModel) : UserModel {
        return userRepository.save(user)
    }
}