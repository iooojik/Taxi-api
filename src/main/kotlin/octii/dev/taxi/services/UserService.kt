package octii.dev.taxi.services

import octii.dev.taxi.models.UserModel
import octii.dev.taxi.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserModel> = userRepository.findAll()

    fun registerUser(user : UserModel) : UserModel {
        return userRepository.save(user)
    }

    fun loginWithToken(user: UserModel) : UserModel{
        val foundUser = userRepository.findByToken(user.token)
        return if (foundUser != null){
            foundUser.token = UUID.randomUUID().toString()
            userRepository.save(foundUser)
        } else {
            val md = UserModel()
            md.token = ""
            return md
        }
    }

    fun login(user: UserModel): UserModel {
        val foundUser = userRepository.findByPhone(user.phone)
        return if (foundUser != null){
            foundUser.isWhatsapp = user.isWhatsapp
            foundUser.isViber = user.isViber
            foundUser.token = UUID.randomUUID().toString()
            userRepository.save(foundUser)
        } else userRepository.save(user)
    }

    fun getByPhoneNumber(phone : String) : UserModel? = userRepository.findByPhone(phone)
}