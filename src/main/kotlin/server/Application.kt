package server

import dto.CryptoResponse
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
import utils.CryptoUtils
import utils.TLVUtils

fun main() {

    val log = LoggerFactory.getLogger("server")

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }
        routing {
            try {
                post("/process") {
                    val encodedRequest: ByteArray = call.receive()
                    log.info("Request received...")
                    val request = TLVUtils.decodeCryptoRequest(encodedRequest)

                    val result = CryptoUtils.process(request.data, request.key, request.iv, request.algorithm, request.encrypting)

                    val response = CryptoResponse(result)
                    val encodedResponse = TLVUtils.encodeCryptoResponse(response)

                    call.respond(encodedResponse)
                    log.info("Response sent...")
                }
            } catch (e: Exception) {
                log.error("Error processing request", e)
            }
        }
    }.start(wait = true)
}