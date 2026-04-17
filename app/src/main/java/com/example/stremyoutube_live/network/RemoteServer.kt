package com.example.stremyoutube_live.network

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.Serializable

@Serializable
data class StreamStatus(
    val isStreaming: Boolean,
    val currentFile: String,
    val uptime: String,
    val cpuTemp: Float,
    val bitrate: String
)

class RemoteServer(private val onCommand: (String) -> Unit) {
    private var server: ApplicationEngine? = null

    fun start(statusProvider: () -> StreamStatus) {
        server = embeddedServer(CIO, port = 8080) {
            install(ContentNegotiation) {
                json()
            }
            routing {
                get("/status") {
                    call.respond(statusProvider())
                }
                post("/control/{command}") {
                    val command = call.parameters["command"] ?: ""
                    onCommand(command)
                    call.respond(mapOf("status" to "ok", "received" to command))
                }
            }
        }.start(wait = false)
    }

    fun stop() {
        server?.stop(1000, 2000)
    }
}
