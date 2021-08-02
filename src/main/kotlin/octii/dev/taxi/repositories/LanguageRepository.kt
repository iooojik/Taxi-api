package octii.dev.taxi.repositories

import octii.dev.taxi.models.SpeakingLanguagesModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LanguageRepository : JpaRepository<SpeakingLanguagesModel, Long>{
    fun deleteAllByUserId(id : Long)
}