package com.isaac.pokedex_clone.domain.repository

interface CryptoRepository {
    fun encrypt(text: ByteArray): ByteArray

    fun decrypt(encryptedData: ByteArray): ByteArray
}

