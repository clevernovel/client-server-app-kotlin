package dev.clevernovel.tlv.encoder.utils

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoUtils {

    fun process(data: ByteArray, key: ByteArray, iv: ByteArray, algorithm: String, encrypt: Boolean): ByteArray {
        val cipher = Cipher.getInstance(algorithm)
        val secretKeySpec = SecretKeySpec(key, algorithm.split("/")[0])
        val ivParameterSpec = IvParameterSpec(iv)
        cipher.init(if (encrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        return cipher.doFinal(data)
    }
}
