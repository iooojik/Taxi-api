package octii.dev.taxi.services

import octii.dev.taxi.models.SpeakingLanguagesModel
import octii.dev.taxi.models.UserModel
import octii.dev.taxi.repositories.LanguageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LanguageService(private val languageRepository: LanguageRepository) {

    fun deleteAllLanguages(userId : Long) = languageRepository.deleteAllByUserId(userId)

    fun deleteAll(userModel: UserModel) = languageRepository.deleteAllByUser(userModel)

    fun save(lang : SpeakingLanguagesModel) : SpeakingLanguagesModel = languageRepository.save(lang)
}