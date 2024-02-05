package com.isaac.pokedex_clone.data.repository

import com.google.crypto.tink.Aead
import com.isaac.pokedex_clone.domain.repository.CryptoRepository
import javax.inject.Inject

class CryptoImpl @Inject constructor(
    private val aead: Aead,
) : CryptoRepository {
    override fun encrypt(text: ByteArray): ByteArray =
        aead.encrypt(text, null)

    override fun decrypt(encryptedData: ByteArray): ByteArray =
        if (encryptedData.isNotEmpty()) {
            aead.decrypt(encryptedData, null)
        } else {
            encryptedData
        }
}