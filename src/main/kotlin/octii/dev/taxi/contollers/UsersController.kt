package octii.dev.taxi.contollers

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.models.OrdersModel
import octii.dev.taxi.models.SpeakingLanguagesModel
import octii.dev.taxi.models.TokenAuthorization
import octii.dev.taxi.models.UserModel
import octii.dev.taxi.repositories.UserRepository
import octii.dev.taxi.services.LanguageService
import octii.dev.taxi.services.OrdersService
import octii.dev.taxi.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.util.DigestUtils
import org.springframework.web.bind.annotation.*
import java.lang.StringBuilder
import java.util.*


@RestController
@Controller
@RequestMapping("/users/")
class UsersController(private val userService: UserService,
                      private val ordersService: OrdersService,
                      private val languageService: LanguageService) : ResponseGenerator {

    @GetMapping("/")
    fun getUsers() : ResponseEntity<Any> = okResponse(userService.getAllUsers())

    @GetMapping("/orders")
    fun getOrders() : ResponseEntity<Any> = okResponse(ordersService.getAll())

    @PostMapping("/registration")
    fun registration(@RequestBody userModel: UserModel) : ResponseEntity<Any> {
        val user = userService.registerUser(userModel)
        return if (user != null) okResponse(user)
        else return errorResponse()
    }

    @PostMapping("/login.token")
    fun loginWithToken(@RequestBody user : UserModel) : ResponseEntity<Any>{
        val userModel = userService.loginWithToken(user)
        return if (userModel != null) {
            val orderModel: OrdersModel? = if (userModel.type == "driver")
                ordersService.getByDriverID(userModel.id)
            else ordersService.getByCustomerID(userModel.id)
            val tokenAuthorization = TokenAuthorization(userModel, orderModel)
            okResponse(tokenAuthorization)
        }else errorResponse("Token error")
    }


    @PostMapping("/login")
    fun login(@RequestBody user : UserModel) : ResponseEntity<Any>{
        val userModel = userService.login(user)
        return if (userModel != null) okResponse(userModel)
        else return errorResponse()
    }

    @PostMapping("/update")
    fun update(@RequestBody user : UserModel) : ResponseEntity<Any>{
        val userModel = userService.getByUserUUID(user.uuid)
        return if (userModel != null){
            userModel.isViber = user.isViber
            userModel.isWhatsapp = user.isWhatsapp
            userModel.type = user.type
            userModel.coordinates?.longitude = user.coordinates?.longitude!!
            userModel.coordinates?.latitude = user.coordinates?.latitude!!
            userModel.driver?.isWorking = user.driver?.isWorking!!
            userModel.driver?.rideDistance = user.driver?.rideDistance!!
            userModel.driver?.pricePerKm = user.driver?.pricePerKm!!
            userModel.driver?.pricePerMinute = user.driver?.pricePerMinute!!
            userModel.driver?.priceWaitingMin = user.driver?.priceWaitingMin!!

            user.languages.forEach { lang ->
                val l = lang.language
                println(l)
                var found = false
                userModel.languages.forEach {
                    if (it.language == l) {
                        found = true
                    }
                }
                println(found)
                if (!found){
                    val savedLang = languageService.save(SpeakingLanguagesModel(language = l))
                    savedLang.user = userModel
                    languageService.save(savedLang)
                }
                found = false
            }
            //userModel.languages = user.languages
            //userModel.driver = user.driver
            okResponse(userService.update(userModel))
        } else errorResponse()

    }
}