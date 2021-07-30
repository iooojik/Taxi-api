package octii.dev.taxi.contollers

import octii.dev.taxi.listeners.WebSocketEventListener
import octii.dev.taxi.models.MessageType
import octii.dev.taxi.models.ResponseModel
import octii.dev.taxi.models.UserModel
import octii.dev.taxi.services.UserService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class SocketController(val simpMessagingTemplate : SimpMessagingTemplate, val userService: UserService) {

    val logger = WebSocketEventListener.logger

    @MessageMapping("/authorization.{uuid}")
    fun authorization(@Payload userModel: UserModel, @DestinationVariable("uuid") uuid : String) : UserModel{
        val model = userService.registerUser(user = userModel)
        logger.info("$model $userModel")
        simpMessagingTemplate.convertAndSend("/topic/$uuid", ResponseModel(MessageType.AUTHORIZATION, model))
        return model ?: UserModel()
    }


}