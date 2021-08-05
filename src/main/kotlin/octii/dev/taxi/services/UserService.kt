package octii.dev.taxi.services

import octii.dev.taxi.constants.Static
import octii.dev.taxi.models.*
import octii.dev.taxi.repositories.CoordinatesRepository
import octii.dev.taxi.repositories.DriverAvailableRepository
import octii.dev.taxi.repositories.LanguageRepository
import octii.dev.taxi.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val userRepository: UserRepository,
                  val driverAvailableRepository: DriverAvailableRepository,
                  val languageService: LanguageService,
                  val coordinatesRepository: CoordinatesRepository,
                  val languageRepository: LanguageRepository,
                  val ordersService: OrdersService, val usersToFilesService: UsersToFilesService) {

    fun getAllUsers(): List<UserModel> = userRepository.findAll()

    fun registerUser(user : UserModel) : UserModel {

        //сохраняем пользователя и получаем его стандартные данные
        //сохраняем модель с координатами
        user.coordinates = null
        user.driver = null
        user.languages = null
        if (user.type == Static.DRIVER_TYPE) user.isOnlyClient = false
        var savedUser = userRepository.save(user)
        savedUser.coordinates = coordinatesRepository.save(CoordinatesModel(user = savedUser))
        savedUser.languages = listOf(languageRepository.save(SpeakingLanguagesModel(user = savedUser)))
        savedUser.driver = driverAvailableRepository.save(DriverAvailableModel(driver = savedUser))
        savedUser.token = UUID.randomUUID().toString()
        savedUser.uuid = UUID.randomUUID().toString()
        //обновляем данные пользователя
        savedUser = userRepository.save(savedUser)
        return savedUser
    }

    fun login(user: UserModel): AuthorizationModel {
        //находим пользователя по номеру телефона или находим по UUID
        var foundUser = userRepository.findByPhone(user.phone) ?: userRepository.findByUuid(user.uuid)
        return if (foundUser != null) {
            if (user.userName.trim().isNotEmpty())
                foundUser.userName = user.userName

            foundUser.isWhatsapp = user.isWhatsapp
            foundUser.isViber = user.isViber
            foundUser.token = UUID.randomUUID().toString().replace("-", "")

            foundUser = userRepository.save(foundUser)
            AuthorizationModel(changeFilesToResp(foundUser), getLastOrder(foundUser))
        } else {
            //если такого пользователя не существует, то создаём нового
            val registered = registerUser(user)
            AuthorizationModel(changeFilesToResp(registered), getLastOrder(registered))
        }
    }

    fun loginWithToken(user: UserModel) : AuthorizationModel{
        var foundUser = userRepository.findByToken(user.token)
        return if (foundUser != null){
            foundUser.token = UUID.randomUUID().toString()
            foundUser = userRepository.save(foundUser)
            AuthorizationModel(changeFilesToResp(foundUser), getLastOrder(foundUser))
        } else AuthorizationModel(foundUser)
    }

    fun getLastOrder(userModel: UserModel): OrdersModel =
        if (userModel.type == Static.DRIVER_TYPE)
            ordersService.getOrderByDriverID(userModel.id)
        else
            ordersService.getOrderByCustomerID(userModel.id)

    fun getByPhoneNumber(phone : String) : UserModel? = userRepository.findByPhone(phone)

    fun getByUserUUID(uuid : String) : UserModel? = userRepository.findByUuid(uuid)

    fun update(oldUser : UserModel) : UserModel {
        val userModel = getByUserUUID(oldUser.uuid)
        if (userModel != null) {
            userModel.isViber = oldUser.isViber
            userModel.isWhatsapp = oldUser.isWhatsapp
            userModel.type = oldUser.type
            userModel.coordinates?.longitude = oldUser.coordinates?.longitude!!
            userModel.coordinates?.latitude = oldUser.coordinates?.latitude!!
            userModel.driver?.isWorking = oldUser.driver?.isWorking!!
            userModel.driver?.rideDistance = oldUser.driver?.rideDistance!!
            userModel.driver?.pricePerKm = oldUser.driver?.pricePerKm!!
            userModel.driver?.pricePerMinute = oldUser.driver?.pricePerMinute!!
            userModel.driver?.priceWaitingMin = oldUser.driver?.priceWaitingMin!!
            languageService.deleteAllLanguages(userModel.id)
            val savedLanguages = arrayListOf<SpeakingLanguagesModel>()
            if (oldUser.languages?.isNotEmpty() == true) {

                oldUser.languages?.forEach { l ->
                    var found = false

                    savedLanguages.forEach {
                        if (it.language == l.language) found = true
                    }
                    if (!found) {
                        savedLanguages.add(saveLanguage(l.language, userModel))
                    }
                    found = false
                }

            } else {
                saveLanguage(userModel = userModel)
            }
            return changeFilesToResp(userRepository.save(userModel))
        } else return UserModel()
    }

    private fun changeFilesToResp(newUser: UserModel) : UserModel{
        newUser.files = usersToFilesService.getAllNew(newUser.id)
        return newUser
    }

    private fun saveLanguage(language : String = "sr", userModel: UserModel) : SpeakingLanguagesModel{
        val savedLang = languageService.save(SpeakingLanguagesModel(language = language, userId = userModel.id, user = userModel))
        savedLang.user = userModel
        return languageService.save(savedLang)
    }

    fun changeAvatar(user : UserModel, avatarURL : String){
        val foundUser = userRepository.findByToken(user.token)
        if (foundUser != null){
            foundUser.avatarURL = avatarURL
            userRepository.save(foundUser)
        }
    }
}