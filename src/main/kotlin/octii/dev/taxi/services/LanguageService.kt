package octii.dev.taxi.services

import octii.dev.taxi.models.database.SpeakingLanguagesModel
import octii.dev.taxi.models.database.UserModel
import octii.dev.taxi.repositories.LanguageRepository
import org.springframework.stereotype.Service

@Service
class LanguageService(private val languageRepository: LanguageRepository) {

    fun deleteAllLanguages(userId : Long) = languageRepository.deleteAllByUserId(userId)

    fun deleteAll(userModel: UserModel) = languageRepository.deleteAllByUser(userModel)

    fun save(lang : SpeakingLanguagesModel) : SpeakingLanguagesModel = languageRepository.save(lang)
}