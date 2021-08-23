package octii.dev.taxi.contollers.rest

import octii.dev.taxi.constants.Static
import octii.dev.taxi.constants.StaticMessages
import octii.dev.taxi.custom.ResponseGenerator
import octii.dev.taxi.models.database.UsersToFiles
import octii.dev.taxi.services.UserService
import octii.dev.taxi.services.UsersToFilesService
import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.util.DigestUtils
import org.springframework.util.unit.DataSize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.servlet.MultipartConfigElement


@RestController
@Controller
@RequestMapping("/files/")
class FilesController(val userService: UserService, val usersToFilesService: UsersToFilesService) : ResponseGenerator {
	
	@PostMapping("/uploadImage")
	@ResponseBody
	fun handleImageUpload(
		@RequestParam file: MultipartFile, @RequestParam type: String,
		@RequestParam userUUID: String
	): ResponseEntity<Any> {
		return if (!file.isEmpty) {
			val path = "/home/tomcat/taxi/images/${type.lowercase()}/"
			val convertedFile =
				File(
					path + DigestUtils.md5DigestAsHex((file.originalFilename + Date().time).toByteArray())
							+ ".${getExtension(file.originalFilename.toString())}"
				)
			try {
				convertedFile.setReadable(true, false)
				convertedFile.createNewFile()
				val fileOutputStream = FileOutputStream(convertedFile)
				fileOutputStream.write(file.bytes)
				fileOutputStream.close()
				val userModel = userService.getByUserUUID(userUUID)
				if (userModel != null) {
					val url = "https://iooojik.ru/taxi/images/$type/${convertedFile.name}"
					when (type.lowercase()) {
						Static.IMAGE_AVATAR_TYPE -> {
							userService.changeAvatar(userModel, url)
						}
						Static.IMAGE_CAR_TYPE -> {
						
						}
						Static.IMAGE_CAR_NUMBER_TYPE -> {
						
						}
						Static.IMAGE_LICENSE_TYPE -> {
						
						}
					}
					var f = usersToFilesService.saveFile(
						UsersToFiles(
							url = url, fileName = convertedFile.name,
							fileExtension = convertedFile.extension, type = type.lowercase()
						)
					)
					f.user = userModel
					f = usersToFilesService.saveFile(f)
					
					okResponse(f)
				} else return errorResponse()
			} catch (e: IOException) {
				e.printStackTrace()
				errorResponse(e.toString())
			}
		} else {
			errorResponse(StaticMessages.EMPTY_FILE)
		}
	}
	
	@Bean
	fun multipartConfigElement(): MultipartConfigElement? {
		val factory = MultipartConfigFactory()
		factory.setMaxFileSize(DataSize.ofBytes(100000000L))
		factory.setMaxRequestSize(DataSize.ofBytes(100000000L))
		return factory.createMultipartConfig()
	}
	
	private fun getExtension(fileName: String): String = fileName.substringAfterLast('.', ".")
	
}