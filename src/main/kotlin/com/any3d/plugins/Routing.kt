package com.any3d.plugins

import com.any3d.services.VuforiaApi
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val serverAccessKey = "df15ae87939e346784dc41d1fc939b27bdb31ac7"
val serverSecretKey = "1eb77208f29f898dcf58e6b2625d70964afc5e14"

fun Application.configureRouting() {
    routing {
        get("/") {
            runBlocking {
                launch {
                    val vuforiaApi = VuforiaApi()
                    vuforiaApi.getAlLTargets(serverAccessKey, serverSecretKey)
                }
            }
            call.respondText("Hello, world!")
        }
    }
}
