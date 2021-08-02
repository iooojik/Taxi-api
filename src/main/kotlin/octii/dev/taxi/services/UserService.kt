package octii.dev.taxi.services

import octii.dev.taxi.models.DriverAvailableModel
import octii.dev.taxi.models.UserModel
import octii.dev.taxi.repositories.DriverAvailableRepository
import octii.dev.taxi.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository,
                  val driverAvailableRepository: DriverAvailableRepository,
                  val languageService: LanguageService) {

    fun getAllUsers(): List<UserModel> = userRepository.findAll()

    fun registerUser(user : UserModel) : UserModel {
        val savedUser = userRepository.save(user)
        if (savedUser.type == "driver")
            driverAvailableRepository.save(DriverAvailableModel(driver = user, driverID = user.id))

        return savedUser
    }

    fun loginWithToken(user: UserModel) : UserModel{
        val foundUser = userRepository.findByToken(user.token)
        return if (foundUser != null){
            foundUser.token = UUID.randomUUID().toString()
            userRepository.save(foundUser)
        } else {
            val md = UserModel()
            md.token = ""
            return md
        }
    }

    fun login(user: UserModel): UserModel {
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
            userRepository.save(foundUser)
        } else userRepository.save(user)
    }

    fun getByPhoneNumber(phone : String) : UserModel? = userRepository.findByPhone(phone)

    fun getByUserUUID(uuid : String) : UserModel? = userRepository.findByUuid(uuid)

    fun update(user : UserModel) : UserModel {
        //languageService.deleteAllLanguages(user.id)
        return userRepository.save(user)
    }
}