package octii.dev.taxi.repositories

import octii.dev.taxi.models.SpeakingLanguagesModel
import octii.dev.taxi.models.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface LanguageRepository : JpaRepository<SpeakingLanguagesModel, Long>{
    @Transactional
    fun deleteAllByUserId(id : Long)

    fun deleteAllByUser(user : UserModel)
}