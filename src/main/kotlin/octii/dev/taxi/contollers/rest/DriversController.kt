package octii.dev.taxi.contollers.rest

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.models.database.DriverModel
import octii.dev.taxi.services.DriverService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Controller
@RequestMapping("/drivers/")
class DriversController(private val driverService: DriverService) : ResponseGenerator {

    //@GetMapping("/")
    //fun getDrivers() : ResponseEntity<Any> = okResponse(driverService.getAll())

    @PostMapping("/driver.get")
    fun getDriver(@RequestBody driver : DriverModel) : ResponseEntity<Any>{
        val driverAvailable = driverService.getByDriverID(driver.driver!!.id)
        return if (driverAvailable != null) okResponse(driverAvailable)
        else errorResponse()
    }

    @PostMapping("/driver.update")
    fun updateDriver(@RequestBody driver : DriverModel) : ResponseEntity<Any>{
        var driverAvailable = driverService.getByDriverID(driver.driver!!.id)
        return if (driverAvailable != null){
            driverAvailable = driverService.update(driverAvailable, driver)
            if (driverAvailable != null) okResponse(driverAvailable)
            else errorResponse()
        } else errorResponse()
    }



}