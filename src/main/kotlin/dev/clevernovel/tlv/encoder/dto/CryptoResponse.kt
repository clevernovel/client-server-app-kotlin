package dev.clevernovel.tlv.encoder.dto

import kotlinx.serialization.Serializable

@Serializable
data class CryptoResponse(val result: ByteArray)