package octii.dev.taxi.repositories

import octii.dev.taxi.models.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserModel, Long> {
    fun findByPhone(phone : String) : UserModel?

    fun findByToken(token : String) : UserModel?

    fun findByUuid(uuid : String) : UserModel?

}