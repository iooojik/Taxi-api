package octii.dev.taxi.contollers.rest

import octii.dev.taxi.ResponseGenerator
import octii.dev.taxi.models.database.LogModel
import octii.dev.taxi.services.LogService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime

@Controller
@RestController
@RequestMapping("/logs/")
class LogController(private val logService: LogService) : ResponseGenerator {
	@PostMapping("/send.log")
	@ResponseBody
	fun sendLog(@RequestParam file: MultipartFile, @RequestParam(name = "userUUID") userUUID : String): ResponseEntity<Any> {
		return if (!file.isEmpty) {
			val today = LocalDateTime.now()
			val path = "/home/tomcat/taxi/logs/${today.year}/${today.month}/${today.dayOfMonth}/${today.hour}/"
			val fileName = file.originalFilename!!
			val logModel = LogModel(path = path, userUUID = userUUID)
			//val path = "./images/${type.lowercase()}/"
			val convertedFile = File("$path$fileName")
			File(path).mkdirs()
			try {
				convertedFile.setReadable(true, true)
				convertedFile.createNewFile()
				val fileOutputStream = FileOutputStream(convertedFile)
				fileOutputStream.write(file.bytes)
				fileOutputStream.close()
				return okResponse(logService.save(logModel))
			} catch (e: IOException) {
				e.printStackTrace()
				errorResponse(e.toString())
			}
		} else {
			errorResponse("empty file")
		}
	}
}