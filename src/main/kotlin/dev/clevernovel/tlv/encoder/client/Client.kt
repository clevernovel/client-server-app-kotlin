package dev.clevernovel.tlv.encoder.client

import dev.clevernovel.tlv.encoder.dto.CryptoRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import dev.clevernovel.tlv.encoder.utils.TLVUtils
import java.io.File
import java.util.*

suspend fun main(args: Array<String>) {

    val log = LoggerFactory.getLogger("client")
    val scanner = Scanner(System.`in`)

    println("Введите путь к файлу данных или оставьте пустым для ввода вручную:")
    val filePath = scanner.nextLine()

    val inputData: ByteArray = if (filePath.isNotEmpty()) {
        try {
            File(filePath).readBytes()
        } catch (e: Exception) {
            log.error("Ошибка при чтении файла: ${e.message}")
            return
        }
    } else {
        println("Введите данные для шифрования/дешифрования:")
        scanner.nextLine().toByteArray()
    }

    println("Введите ключ шифрования (Формат: 1234567812345678):")
    val inputKey = scanner.nextLine().toByteArray()

    println("Введите вектор инициализации (Формат: 1234567812345678):")
    val inputIv = scanner.nextLine().toByteArray()

    println("Введите имя алгоритма шифрования (например, AES/CBC/PKCS5Padding):")
    val inputAlgorithm = scanner.nextLine()

    println("Введите направление операции (true для шифрования, false для дешифрования):")
    val inputEncrypting = scanner.nextLine().toBoolean()

    println("Введите путь к файлу для сохранения результата или оставьте пустым.")
    val outputFilePath = scanner.nextLine()

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }
    }

    try {
        val request = CryptoRequest(inputData, inputKey, inputIv, inputAlgorithm, inputEncrypting)
        val encodedRequest = TLVUtils.encodeCryptoRequest(request)

    val response: HttpResponse = client.post("http://localhost:8080/process") {
        contentType(ContentType.Application.Json)
        setBody(encodedRequest)
    }

    if (response.status == HttpStatusCode.OK) {
        val encodedCryptoResponse: ByteArray = response.body()
        val cryptoResponse = TLVUtils.decodeCryptoResponse(encodedCryptoResponse)

        if (outputFilePath.isNotEmpty()) {
            try {
                File(outputFilePath).writeBytes(cryptoResponse.result)
            } catch (e: Exception) {
                log.error("Ошибка при записи в файл: ${e.message}")
            }
        }
        log.info("Результат: ${String(cryptoResponse.result)}")
    } else {
        log.error("Ошибка: ${response.status}")
    }

    } catch (e: Exception) {
        log.error("Произошла ошибка: ${e.message}")
    } finally {
        client.close()
    }
}
