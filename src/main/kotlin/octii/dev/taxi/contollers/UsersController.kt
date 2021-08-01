package octii.dev.taxi.contollers

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.models.OrdersModel
import octii.dev.taxi.models.TokenAuthorization
import octii.dev.taxi.models.UserModel
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
class UsersController(private val userService: UserService, private val ordersService: OrdersService) : ResponseGenerator {

    @GetMapping("/")
    fun getUsers() : ResponseEntity<Any> = okResponse(userService.getAllUsers())

    @PostMapping("/registration")
    fun registration(@RequestBody userModel: UserModel) : ResponseEntity<Any> {
        return okResponse(userService.registerUser(userModel))
    }

    @PostMapping("/login")
    fun login(@RequestBody user : UserModel) : ResponseEntity<Any>{
        return okResponse(userService.login(user))
    }

    @PostMapping("/update")
    fun update(@RequestBody user : UserModel) : ResponseEntity<Any>{
        val userModel = userService.getByUserUUID(user.uuid)
        return if (userModel != null){
            userModel.isViber = user.isViber
            userModel.isWhatsapp = user.isWhatsapp
            userModel.type = user.type
            userModel.languages = user.languages
            //userModel.language = user.language
            okResponse(userService.update(userModel))
        } else errorResponse()

    }

    @PostMapping("/login.token")
    fun loginWithToken(@RequestBody user : UserModel) : ResponseEntity<Any>{
        val userModel = userService.loginWithToken(user)
        var orderModel : OrdersModel? = null
        orderModel = if (userModel.type == "driver")
            ordersService.getByDriverID(userModel.id)
        else ordersService.getByCustomerID(userModel.id)
        val tokenAuthorization = TokenAuthorization(userModel, orderModel)
        println(tokenAuthorization)
        return okResponse(tokenAuthorization)
    }

}