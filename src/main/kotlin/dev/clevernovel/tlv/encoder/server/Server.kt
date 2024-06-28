package dev.clevernovel.tlv.encoder.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import dev.clevernovel.tlv.encoder.utils.CryptoUtils
import dev.clevernovel.tlv.encoder.utils.TLVUtils
import dev.clevernovel.tlv.encoder.dto.CryptoResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.module() {
    val log = LoggerFactory.getLogger("server")

    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }

    routing {
        post("/process") {
            try {
                val encodedRequest: ByteArray = call.receive()
                log.info("Request received...")
                val request = TLVUtils.decodeCryptoRequest(encodedRequest)

                val result = CryptoUtils.process(request.data, request.key, request.iv, request.algorithm, request.encrypting)

                val response = CryptoResponse(result)
                val encodedResponse = TLVUtils.encodeCryptoResponse(response)

                call.respond(encodedResponse)
                log.info("Response sent...")
            } catch (e: Exception) {
                log.error("Error processing request", e)
                call.respond(HttpStatusCode.BadRequest, "Invalid request data")
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}
