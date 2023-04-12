package com.github.saenai255.services

import com.github.saenai255.protobuf.*
import io.ktor.server.websocket.*

class FSPlayerService(val socket: DefaultWebSocketServerSession) : AbstractFSPlayerService() {

    override fun getPlayer(param: PlayerId): Player {
        val playerId = param.value

        return Player.newBuilder()
            .setId(playerId)
            .setLevel(20)
            .build()
    }

    override fun ping(param: FSPing): FSPong =
        FSPong.newBuilder().setValue(param.value).build()
}