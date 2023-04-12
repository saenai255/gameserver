package com.github.saenai255.services

import com.github.saenai255.protobuf.AbstractFCPlayerService
import com.github.saenai255.protobuf.PlayerId
import com.github.saenai255.protobuf.ProcResult
import com.github.saenai255.protobuf.ProcResultType

class FCPlayerService : AbstractFCPlayerService() {
    override fun levelUp(param: PlayerId): ProcResult {
        return ProcResult.newBuilder()
            .setType(ProcResultType.FAIL_UNKNOWN)
            .setMessage("Unknown player")
            .build()
    }
}