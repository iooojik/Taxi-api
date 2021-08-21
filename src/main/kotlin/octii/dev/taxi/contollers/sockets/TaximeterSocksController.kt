package octii.dev.taxi.contollers.sockets

import octii.dev.taxi.constants.TaximeterStatus
import octii.dev.taxi.constants.TaximeterType
import octii.dev.taxi.listeners.WebSocketEventListener
import octii.dev.taxi.models.TaximeterUpdate
import octii.dev.taxi.models.database.TaximeterModel
import octii.dev.taxi.models.sockets.TaximeterResponseModel
import octii.dev.taxi.services.TaximeterService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class TaximeterSocksController(val simpMessagingTemplate : SimpMessagingTemplate, val taximeterService: TaximeterService) {

    val logger = WebSocketEventListener.logger

    @MessageMapping("/taximeter.start.{uuid}")
    fun taximeterStart(@Payload taximeterUpdate: TaximeterUpdate, @DestinationVariable("uuid") userUUID: String){
        taximeterService.save(TaximeterModel(
            timeStamp = Date().toString(),
            action = TaximeterStatus.ACTION_STARTING_ROUTE,
            orderUUID = taximeterUpdate.orderUUID
        ))
        simpMessagingTemplate.convertAndSend(
            "/topic/${taximeterUpdate.recipientUUID}/taximeter",
            TaximeterResponseModel(TaximeterType.TAXIMETER_START, coordinates = taximeterUpdate.coordinates,
                status = TaximeterStatus.ACTION_STARTING_ROUTE)
        )
    }

    @MessageMapping("/taximeter.stop.{uuid}")
    fun taximeterStop(@Payload taximeterUpdate: TaximeterUpdate, @DestinationVariable("uuid") userUUID: String){
        taximeterService.save(TaximeterModel(
            timeStamp = Date().toString(),
            action = TaximeterStatus.ACTION_FINISHING_ROUTE,
            orderUUID = taximeterUpdate.orderUUID
        ))
        simpMessagingTemplate.convertAndSend(
            "/topic/${taximeterUpdate.recipientUUID}/taximeter",
            TaximeterResponseModel(TaximeterType.TAXIMETER_STOP, coordinates = taximeterUpdate.coordinates,
                status = TaximeterStatus.ACTION_FINISHING_ROUTE)
        )
    }

    @MessageMapping("/taximeter.update.coordinates.{uuid}")
    fun taximeterUpdate(@Payload taximeterUpdate: TaximeterUpdate, @DestinationVariable("uuid") userUUID: String){
        simpMessagingTemplate.convertAndSend(
            "/topic/${taximeterUpdate.recipientUUID}/taximeter",
            TaximeterResponseModel(TaximeterType.TAXIMETER_UPDATE, coordinates = taximeterUpdate.coordinates)
        )
    }

    @MessageMapping("/taximeter.waiting.{uuid}.{waiting}")
    fun taximeterWaiting(@Payload taximeterUpdate: TaximeterUpdate, @DestinationVariable("uuid") userUUID: String,
                         @DestinationVariable("waiting") isWaiting : Boolean){
        simpMessagingTemplate.convertAndSend(
            "/topic/${taximeterUpdate.recipientUUID}/taximeter",
            TaximeterResponseModel(TaximeterType.TAXIMETER_WAITING, coordinates = taximeterUpdate.coordinates, isWaiting = isWaiting)
        )
    }

}