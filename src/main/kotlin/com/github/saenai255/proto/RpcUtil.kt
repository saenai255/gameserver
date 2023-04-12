package com.github.saenai255.proto

import com.github.saenai255.protobuf.FSServiceKind
import com.github.saenai255.protobuf.FSServiceMethod
import com.github.saenai255.protobuf.IFSPlayerService
import com.google.protobuf.ByteString
import com.google.protobuf.Descriptors
import com.google.protobuf.Message

object RpcUtil {
    fun parseParameter(methodDescriptor: Descriptors.MethodDescriptor, bytes: ByteString): Message {
        val clazz = Class.forName("com.github.saenai255.protobuf.${methodDescriptor.inputType.fullName}").asSubclass(
            Message::class.java)
        val func = clazz.getMethod("parseFrom", ByteString::class.java)
        return func(null, bytes) as Message
    }

    fun parseReturn(methodDescriptor: Descriptors.MethodDescriptor): Message {
        val clazz = Class.forName("com.github.saenai255.protobuf.${methodDescriptor.outputType.fullName}").asSubclass(
            Message::class.java)
        val func = clazz.getMethod("getDefaultInstance")
        return func(null) as Message
    }

    fun getMethodDescriptor(serviceKind: FSServiceKind, method: FSServiceMethod): Descriptors.MethodDescriptor? =
        when(serviceKind) {
            FSServiceKind.FS_SERVICE_PLAYER -> {
                when (method) {
                    FSServiceMethod.FS_GET_PLAYER -> IFSPlayerService.getDescriptor().findMethodByName("getPlayer")
                    else -> null
                }
            }
            else -> null
        }
}