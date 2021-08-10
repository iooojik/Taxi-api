package octii.dev.taxi.services

import octii.dev.taxi.constants.Static
import octii.dev.taxi.models.AuthorizationModel
import octii.dev.taxi.models.Prices
import octii.dev.taxi.models.database.*
import octii.dev.taxi.repositories.CoordinatesRepository
import octii.dev.taxi.repositories.DriverRepository
import octii.dev.taxi.repositories.LanguageRepository
import octii.dev.taxi.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class UserService(val userRepository: UserRepository,
                  val driverRepository: DriverRepository,
                  val languageService: LanguageService,
                  val coordinatesRepository: CoordinatesRepository,
                  val languageRepository: LanguageRepository,
                  val ordersService: OrdersService,
                  val usersToFilesService: UsersToFilesService, val pricesService: PricesService) {

    fun getAllUsers(): List<UserModel> = userRepository.findAll()

    fun registerUser(user : UserModel) : UserModel {
        //сохраняем пользователя и получаем его стандартные данные
        //сохраняем модель с координатами
        val coords = user.coordinates
        user.coordinates = null
        user.driver = null
        user.languages = null
        if (user.type == Static.DRIVER_TYPE) user.isOnlyClient = false
        var savedUser = userRepository.save(user)
        savedUser.coordinates = coordinatesRepository.save(CoordinatesModel(user = savedUser))
        savedUser.coordinates!!.longitude = coords!!.longitude
        savedUser.coordinates!!.latitude = coords.latitude
        coordinatesRepository.save(savedUser.coordinates!!)
        savedUser.languages = listOf(languageRepository.save(SpeakingLanguagesModel(user = savedUser)))
        savedUser.driver = driverRepository.save(DriverModel(driver = savedUser))
        savedUser.driver!!.prices = pricesService.save(Prices(driver = savedUser.driver))
        savedUser.token = UUID.randomUUID().toString()
        savedUser.uuid = UUID.randomUUID().toString()
        savedUser.lastLogin = Date().toString()
        //обновляем данные пользователя
        savedUser = userRepository.save(savedUser)
        println(savedUser)
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
            foundUser.lastLogin = Date().toString()

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
            foundUser.lastLogin = Date().toString()
            foundUser = userRepository.save(foundUser)
            val lastOrder = getLastOrder(foundUser)
            AuthorizationModel(changeFilesToResp(foundUser), if (lastOrder.isFinished) null else lastOrder)
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
            userModel.driver?.rideDistance = oldUser.driver?.rideDistance!!
            userModel.driver?.prices?.pricePerKm = if (oldUser.driver?.prices?.pricePerKm != null) oldUser.driver?.prices?.pricePerKm!! else 10f
            userModel.driver?.prices?.pricePerMinute =
                if (oldUser.driver?.prices?.pricePerMinute != null) oldUser.driver?.prices?.pricePerMinute!! else 1f
            userModel.driver?.prices?.priceWaitingMin =
                if (oldUser.driver?.prices?.priceWaitingMin != null) oldUser.driver?.prices?.priceWaitingMin!! else 1f

            userModel.driver!!.isWorking = if (userModel.type == Static.CLIENT_TYPE) false else oldUser.driver!!.isWorking
            userModel.languages = updateLanguages(userModel.languages!!, oldUser.languages!!, userModel)

            return changeFilesToResp(userRepository.save(userModel))
        } else return UserModel()
    }

    private fun updateLanguages(oldLanguagesList : List<SpeakingLanguagesModel>,
                                newLanguagesList : List<SpeakingLanguagesModel>,
                                user : UserModel) : List<SpeakingLanguagesModel>{
        //создаём список с языками
        val l = arrayListOf<String>()
        oldLanguagesList.forEach { l.add(it.language) }
        newLanguagesList.forEach { l.add(it.language) }
        val languages = l.stream().distinct().collect(Collectors.toList())
        val returnList = arrayListOf<SpeakingLanguagesModel>()
        languages.forEach {
            returnList.add(saveLanguage(it, user))
        }
        return returnList
    }

    private fun changeFilesToResp(newUser: UserModel) : UserModel {
        newUser.files = usersToFilesService.getAllNew(newUser.id)
        return newUser
    }

    private fun saveLanguage(language : String = "sr", userModel: UserModel) : SpeakingLanguagesModel {
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