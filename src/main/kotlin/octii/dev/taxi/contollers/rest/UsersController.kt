package octii.dev.taxi.contollers.rest

import octii.dev.taxi.constants.StaticMessages
import octii.dev.taxi.custom.ResponseGenerator
import octii.dev.taxi.models.database.UserModel
import octii.dev.taxi.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@Controller
@RequestMapping("/users/")
class UsersController(
	private val userService: UserService
) : ResponseGenerator {
	
	@PostMapping("/login.token")
	fun loginWithToken(@RequestBody user: UserModel): ResponseEntity<Any> {
		//авторизация с помощью токена(передаётся только токен!)
		val authorizationModel = userService.loginWithToken(user)
		return if (authorizationModel.user != null && authorizationModel.user.id > 0) {
			okResponse(authorizationModel)
		} else errorResponse(StaticMessages.TOKEN_ERROR)
	}
	
	@PostMapping("/login")
	fun login(@RequestBody user: UserModel): ResponseEntity<Any> {
		if (user.phone.trim().isNotEmpty()) {
			//авторизация или регистрация
			val authorizationModel = userService.login(user)
			if (authorizationModel.user != null && authorizationModel.user.id > 0)
				return okResponse(authorizationModel)
		}
		return errorResponse()
	}
	
	@PostMapping("/update")
	fun update(@RequestBody user: UserModel): ResponseEntity<Any> {
		return if (user.uuid.trim().isNotEmpty() && user.id > 0) {
			okResponse(userService.update(user))
		} else errorResponse()
	}
	
	@PostMapping("/update.driver.state")
	fun updateDriverState(@RequestBody user: UserModel): ResponseEntity<Any> {
		return if (user.uuid.trim().isNotEmpty()) {
			val u = userService.updateDriverState(user)
			if (u.id > 0) okResponse("")
			else errorResponse()
		} else errorResponse()
	}
}