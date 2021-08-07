package octii.dev.taxi.repositories

import octii.dev.taxi.models.database.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable

@Repository
interface UserRepository : JpaRepository<UserModel, Long>, Serializable {
    @Transactional
    fun findByPhone(phone : String) : UserModel?

    @Transactional
    fun findByToken(token : String) : UserModel?

    @Transactional
    fun findByUuid(uuid : String) : UserModel?

}