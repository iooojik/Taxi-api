package octii.dev.taxi.listeners

import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.jboss.logging.Logger


@Component
class WebSocketEventListener(val messagingTemplate: SimpMessageSendingOperations) {
    companion object {
        val logger: Logger = LoggerFactory.logger(WebSocketEventListener::class.java)
    }

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent?) {
        logger.info("Received a new web socket connection ${event.toString()}")
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        logger.info("Somebody has disconnected ${event.toString()}")
    }
}