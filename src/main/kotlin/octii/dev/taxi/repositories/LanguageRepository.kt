package octii.dev.taxi.repositories

import octii.dev.taxi.models.database.SpeakingLanguagesModel
import octii.dev.taxi.models.database.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface LanguageRepository : JpaRepository<SpeakingLanguagesModel, Long>{
    fun deleteByUser_Id(id : Long)
}