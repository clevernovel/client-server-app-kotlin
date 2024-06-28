package dev.clevernovel.tlv.encoder.dto

import kotlinx.serialization.Serializable

@Serializable
data class CryptoRequest(
    val data: ByteArray,
    val key: ByteArray,
    val iv: ByteArray,
    val algorithm: String,
    val encrypting: Boolean
)