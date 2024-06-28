package dev.clevernovel.tlv.encoder.test.client

import dev.clevernovel.tlv.encoder.dto.CryptoRequest
import dev.clevernovel.tlv.encoder.dto.CryptoResponse
import dev.clevernovel.tlv.encoder.utils.TLVUtils
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClientTest {
    @Test
    fun testClientRequest() = runBlocking {
        val mockEngine = MockEngine { request ->
            respond(
                content = TLVUtils.encodeCryptoResponse(CryptoResponse("EncryptedData".toByteArray())),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val request = CryptoRequest(
            data = "Hello, World!".toByteArray(),
            key = "1234567812345678".toByteArray(),
            iv = "1234567812345678".toByteArray(),
            algorithm = "AES/CBC/PKCS5Padding",
            encrypting = true
        )
        val encodedRequest = TLVUtils.encodeCryptoRequest(request)

        val response: HttpResponse = client.post("http://localhost:8080/process") {
            contentType(ContentType.Application.Json)
            setBody(encodedRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val encodedCryptoResponse: ByteArray = response.body()
        val cryptoResponse = TLVUtils.decodeCryptoResponse(encodedCryptoResponse)
        assertTrue(cryptoResponse.result.isNotEmpty())

        client.close()
    }
}
