package com.github.saenai255

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.github.saenai255.plugins.*
import com.github.saenai255.routes.configureRoutes
import com.github.saenai255.services.FSPlayerService
import org.slf4j.LoggerFactory

const val SERVER_PORT = 50624;

fun main() {
    LoggerFactory.getLogger("main").info("Starting game server...")
     embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
         .start(wait = true)
}


fun Application.module() {
    configureSockets()
    configureRoutes()
}
