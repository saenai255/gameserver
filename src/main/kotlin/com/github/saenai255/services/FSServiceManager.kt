package com.github.saenai255.services

import com.github.saenai255.protobuf.*
import com.google.protobuf.ByteString
import com.google.protobuf.Message
import io.ktor.server.websocket.*

class FSServiceManager(socket: DefaultWebSocketServerSession) {
    private val playerService: AbstractFSPlayerService = FSPlayerService(socket)

    fun handleRemoteProcedureCall(message: ProtoMessage): Message? = when(message.service) {
        FSServiceKind.FS_SERVICE_PLAYER -> handlePlayerServiceCall(message.method, message.parameter)
        else -> null
    }

    private fun handlePlayerServiceCall(method: FSServiceMethod, parameter: ByteString): Message? = when(method) {
        FSServiceMethod.FS_GET_PLAYER -> playerService.getPlayer(PlayerId.parseFrom(parameter))
        FSServiceMethod.FS_PING -> playerService.ping(FSPing.parseFrom(parameter))
        else -> null
    }
}