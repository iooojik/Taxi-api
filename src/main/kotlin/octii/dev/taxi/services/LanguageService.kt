package octii.dev.taxi.services

import octii.dev.taxi.models.SpeakingLanguagesModel
import octii.dev.taxi.repositories.LanguageRepository
import org.springframework.stereotype.Service

@Service
class LanguageService(private val languageRepository: LanguageRepository) {
    fun deleteAllLanguages(userId : Long) = languageRepository.deleteAllByUserId(userId)

    fun save(lang : SpeakingLanguagesModel) : SpeakingLanguagesModel = languageRepository.save(lang)
}