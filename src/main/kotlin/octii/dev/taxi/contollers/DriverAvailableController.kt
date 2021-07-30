package octii.dev.taxi.contollers

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.models.UserModel
import octii.dev.taxi.services.DriverAvailableService
import octii.dev.taxi.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@Controller
@RequestMapping("/drivers.available/")
class DriverAvailableController(private val userService: UserService,
                                private val driverAvailableService: DriverAvailableService) : ResponseGenerator {

    @GetMapping("/")
    fun getDrivers() : ResponseEntity<Any> = okResponse(driverAvailableService.getAll())

}