package com.isaac.pokedex_clone.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.isaac.pokedex.clone.UserLocal
import com.isaac.pokedex_clone.domain.repository.CryptoRepository
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject


class UserLocalSerializer @Inject constructor(
    private val cryptoRepository: CryptoRepository,
) : Serializer<UserLocal> {

    override val defaultValue: UserLocal
        get() = UserLocal.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserLocal {
        try {
            return UserLocal.parseFrom(input.readBytes().let(cryptoRepository::decrypt))
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: UserLocal, output: OutputStream) {
        output.write(cryptoRepository.encrypt(t.toByteArray()))
        output.flush()
    }

}