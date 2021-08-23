package octii.dev.taxi.contollers.sockets

import octii.dev.taxi.constants.MessageType
import octii.dev.taxi.models.database.OrdersModel
import octii.dev.taxi.models.sockets.OrdersResponseModel
import octii.dev.taxi.services.OrdersService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class OrderSocksController(val simpMessagingTemplate: SimpMessagingTemplate, val ordersService: OrdersService) {

}