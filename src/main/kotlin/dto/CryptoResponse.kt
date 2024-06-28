package dto

import kotlinx.serialization.Serializable

@Serializable
data class CryptoResponse(val result: ByteArray)