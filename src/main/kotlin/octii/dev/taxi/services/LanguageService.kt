package octii.dev.taxi.services

import octii.dev.taxi.models.database.SpeakingLanguagesModel
import octii.dev.taxi.repositories.LanguageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LanguageService(private val languageRepository: LanguageRepository) {
	@Transactional
	fun delete(lang: SpeakingLanguagesModel) = languageRepository.delete(lang)
	
	@Transactional
	fun save(lang: SpeakingLanguagesModel): SpeakingLanguagesModel = languageRepository.save(lang)
}