package com.github.saenai255

import io.ktor.server.testing.*
import kotlin.test.*
import com.github.saenai255.plugins.*
import com.github.saenai255.protobuf.*
import com.github.saenai255.routes.configureRoutes
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureSockets()
            configureRoutes()
        }

        val client = createClient {
            install(WebSockets)
        }

        val session = client.webSocketSession("/world")
        val rpcCallPayload = ProtoMessage.newBuilder()
            .setService(FSServiceKind.FS_SERVICE_PLAYER)
            .setMethod(FSServiceMethod.FS_PING)
            .setParameter(
                FSPing.newBuilder().setValue(10).build().toByteString()
            )
            .build()
            .toByteArray()

        session.outgoing.send(Frame.Binary(true, rpcCallPayload))
        val frame = session.incoming.receive()
        if (frame !is Frame.Binary) {
            fail("Message must be binary.")
        }

        val pong = FSPong.parseFrom(frame.data)
        assertEquals(10, pong.value)
    }
}
