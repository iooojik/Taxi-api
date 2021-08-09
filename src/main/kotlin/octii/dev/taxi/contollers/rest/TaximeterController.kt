package octii.dev.taxi.contollers.rest

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.services.TaximeterService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Controller
@RequestMapping("/taximeter/")
class TaximeterController(val taximeterService: TaximeterService) : ResponseGenerator {

    //@GetMapping("/")
    //fun getAll() : ResponseEntity<Any> = okResponse(taximeterService.getAll())

}