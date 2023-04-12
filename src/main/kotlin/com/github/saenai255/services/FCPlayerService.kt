package com.github.saenai255.services

import com.github.saenai255.protobuf.*
import com.google.protobuf.RpcCallback
import com.google.protobuf.RpcController

class FCPlayerService : AbstractFCPlayerService() {
    override fun levelUp(param: PlayerId): ProcResult {
        return ProcResult.newBuilder()
            .setType(ProcResultType.FAIL_UNKNOWN)
            .setMessage("Unknown player")
            .build()
    }
}