package octii.dev.taxi.contollers

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.models.DriverAvailableModel
import octii.dev.taxi.models.UserModel
import octii.dev.taxi.services.DriverAvailableService
import octii.dev.taxi.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@Controller
@RequestMapping("/drivers.available/")
class DriverAvailableController(private val driverAvailableService: DriverAvailableService) : ResponseGenerator {

    @GetMapping("/")
    fun getDrivers() : ResponseEntity<Any> = okResponse(driverAvailableService.getAll())

    @PostMapping("/driver.get")
    fun getDriver(@RequestBody driver : DriverAvailableModel) : ResponseEntity<Any>{
        println(driver)
        val driverAvailable = driverAvailableService.getByDriverID(driver.driver!!.id)
        return if (driverAvailable != null) okResponse(driverAvailable)
        else errorResponse()
    }

    @PostMapping("/driver.update")
    fun updateDriver(@RequestBody driver : DriverAvailableModel) : ResponseEntity<Any>{
        var driverAvailable = driverAvailableService.getByDriverID(driver.driver!!.id)
        println(driverAvailable)
        return if (driverAvailable != null){
            driverAvailable.isWorking = driver.isWorking
            driverAvailable.pricePerKm = driver.pricePerKm
            driverAvailable.pricePerMinute = driver.pricePerMinute
            driverAvailable.priceWaitingMin = driver.priceWaitingMin
            driverAvailable.rideDistance = driver.rideDistance
            driverAvailable = driverAvailableService.save(driverAvailable)
            if (driverAvailable != null) okResponse(driverAvailable)
            else errorResponse()
        } else errorResponse()
    }



}