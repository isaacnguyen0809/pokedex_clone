package com.isaac.pokedex_clone.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.integration.android.AndroidKeystoreKmsClient.*
import com.isaac.pokedex.clone.UserLocal
import com.isaac.pokedex_clone.data.local.UserLocalSerializer
import com.isaac.pokedex_clone.data.repository.AuthRepoImpl
import com.isaac.pokedex_clone.data.repository.CryptoImpl
import com.isaac.pokedex_clone.data.repository.PokemonRepoImpl
import com.isaac.pokedex_clone.data.repository.UserLocalImpl
import com.isaac.pokedex_clone.domain.repository.AuthRepository
import com.isaac.pokedex_clone.domain.repository.CryptoRepository
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import com.isaac.pokedex_clone.domain.repository.UserLocalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun userLocalSerializer(userLocalSerializerImpl: UserLocalSerializer): Serializer<UserLocal>

    @Binds
    fun bindPokemonRepository(repositoryImpl: PokemonRepoImpl): PokemonRepository

    @Binds
    fun bindAuthRepository(repositoryImpl: AuthRepoImpl): AuthRepository

    @Binds
    fun bindUserLocalRepository(userLocalImpl: UserLocalImpl): UserLocalRepository

    @Binds
    fun bindCryptoRepository(cryptoImpl: CryptoImpl): CryptoRepository

    companion object {

        private const val KEY_SET_NAME = "pokedex_key_set_name"
        private const val PREF_FILE_NAME = "pokedex_pref_file_name"
        private const val MASTER_KEY_URI = "${PREFIX}pokedex_master_key"

        @Provides
        @Singleton
        fun providesAEAD(@ApplicationContext context: Context): Aead {
            AeadConfig.register()
            return AndroidKeysetManager
                .Builder()
                .withSharedPref(context, KEY_SET_NAME, PREF_FILE_NAME)
                .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .keysetHandle
                .getPrimitive(Aead::class.java)
        }

        @Provides
        @Singleton
        fun dataStore(
            @ApplicationContext applicationContext: Context,
            serializer: Serializer<UserLocal>,
        ): DataStore<UserLocal> = DataStoreFactory.create(
            serializer = serializer,
            produceFile = { applicationContext.dataStoreFile("user_local") },
        )
    }

}