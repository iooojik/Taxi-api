package octii.dev.taxi.contollers.rest

import octii.dev.taxi.custom.ResponseGenerator
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/*")
class WebController : ResponseGenerator {
	
	@GetMapping("/")
	fun index(): String = "index"
	
	@GetMapping("/privacy*")
	fun privacy(): String = "privacy"
	
	@GetMapping("/terms*")
	fun terms(): String = "terms"
	
	@GetMapping("/images/*/*")
	@ResponseBody
	fun serveFile(request: HttpServletRequest, response: HttpServletResponse) {
		val type = request.requestURL.toString().split("/")[5]
		val fileName = request.requestURL.toString().substring(request.requestURL.toString().lastIndexOf('/') + 1)
		val path = "/home/tomcat/taxi/images/${type.lowercase()}"
		val file = File("$path/$fileName")
		response.setHeader("Content-transfer-Encoding", "binary")
		response.setHeader("content-type", "image/jpeg")
		try {
			val bos = BufferedOutputStream(response.outputStream)
			val fis = FileInputStream(file)
			var len: Int
			val buf = ByteArray(1024)
			while (fis.read(buf).also { len = it } > 0) {
				bos.write(buf, 0, len)
			}
			bos.close()
			response.flushBuffer()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}
}