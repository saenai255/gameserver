package com.github.saenai255.routes

import com.github.saenai255.protobuf.*
import com.github.saenai255.services.FSServiceManager
import com.google.protobuf.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.slf4j.LoggerFactory
import java.util.logging.Logger

fun Application.configureRoutes() {
    val log = LoggerFactory.getLogger(Application::class.java)

    routing {
        webSocket("/world") {
            val serviceManager = FSServiceManager(this)

            for (frame in incoming) {
                if (frame !is Frame.Binary) {
                    continue
                }

                val message = try {
                    ProtoMessage.parseFrom(frame.readBytes())
                } catch (e: Exception) {
                    log.info("Failed to parse received message.", e)
                    close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, "Invalid message format."))
                    continue
                }

                val result: Message? = try {
                    serviceManager.handleRemoteProcedureCall(message)
                } catch (e: Exception) {
                    log.info("Failed to handle ${message.service.name}@${message.method.name}.", e)
                    close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, "Something went wrong."))
                    continue
                }

                if (result != null) {
                    outgoing.send(Frame.Binary(true, result.toByteArray()))
                }
            }
        }
    }
}