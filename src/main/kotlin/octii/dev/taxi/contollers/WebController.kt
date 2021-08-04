package octii.dev.taxi.contollers

import octii.dev.taxi.ResponseGenerator
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@Controller
@RequestMapping("/*")
class WebController : ResponseGenerator {

    @GetMapping("/images/*/*")
    @ResponseBody
    fun serveFile(request : HttpServletRequest, response : HttpServletResponse){
        val type = request.requestURL.toString().split("/")[4]
        val fileName = request.requestURL.toString().substring(request.requestURL.toString().lastIndexOf('/') + 1)
        val file = File("./images/${type.lowercase()}/$fileName")
        //response.setHeader("Content-Disposition", "attachment; filename=${file.name}")
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