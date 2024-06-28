package dev.clevernovel.tlv.encoder.server

import dev.clevernovel.tlv.encoder.dto.CryptoResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import dev.clevernovel.tlv.encoder.utils.CryptoUtils
import dev.clevernovel.tlv.encoder.utils.TLVUtils

fun main() {

    val log = LoggerFactory.getLogger("server")

    embeddedServer(Netty, port = 8080) {
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
    }.start(wait = true)
}