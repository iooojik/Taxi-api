package octii.dev.taxi.contollers

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.models.UserModel
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
class UsersController(private val userService: UserService) : ResponseGenerator {

    @GetMapping("/")
    fun getUsers() : ResponseEntity<Any> = okResponse(userService.getAllUsers())

    @PostMapping("/registration")
    fun registration(@RequestBody userModel: UserModel) : UserModel {
        return userService.registerUser(userModel)
    }

    @PostMapping("/login")
    fun login(@RequestBody user : UserModel) : UserModel{
        return userService.login(user)
    }

    @PostMapping("/login.token")
    fun loginWithToken(@RequestBody user : UserModel) : UserModel{
        return userService.loginWithToken(user)
    }

}