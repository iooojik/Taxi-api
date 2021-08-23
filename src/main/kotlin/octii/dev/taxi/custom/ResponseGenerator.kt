package octii.dev.taxi.custom

import org.springframework.http.ResponseEntity

interface ResponseGenerator {
	
	fun okResponse(model: Any): ResponseEntity<Any> {
		return ResponseEntity.ok().body(model)
	}
	
	fun messageOkResponse(message: String?): ResponseEntity<Any> {
		return ResponseEntity.ok().body(MessageBody(message))
	}
	
	fun errorResponse(message: String? = "", errorCode: Int = 403): ResponseEntity<Any> {
		return ResponseEntity.status(errorCode).body(ErrorBody(message, errorCode))
	}
	
	fun redirect(): String {
		return "<script type=\"text/javascript\"> document.location.href = 'https://iooojik.ru/'; </script>"
	}
	
}

class ErrorBody(val errorMessage: String? = "", val errorCode: Int? = 403)

class MessageBody(val message: String? = "")

