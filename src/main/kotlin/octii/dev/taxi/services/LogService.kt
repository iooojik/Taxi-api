package octii.dev.taxi.services

import octii.dev.taxi.models.database.LogModel
import octii.dev.taxi.repositories.LogRepository
import org.springframework.stereotype.Service

@Service
class LogService(private val logRepository: LogRepository) {
	fun save(logModel: LogModel) : LogModel = logRepository.save(logModel)
}