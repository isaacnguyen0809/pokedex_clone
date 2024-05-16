package com.isaac.pokedex_clone.data.local

import android.content.Context
import androidx.annotation.AnyThread
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.isaac.pokedex_clone.data.local.dao.FavoritePokemonDao
import com.isaac.pokedex_clone.data.local.entity.FavoritePokemonEntity
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Database(
    entities = [
        FavoritePokemonEntity::class,
    ],
    version = 1,
    exportSchema = true
)


abstract class PokemonDatabase : RoomDatabase() {
    abstract fun favoritePokemonDao(): FavoritePokemonDao

    companion object {
        private const val DB_NAME = "pokemon_app.db"

        // Double-checked locking singleton pattern
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        @AnyThread
        @JvmStatic
        fun getInstance(
            context: Context,
            queryExecutor: Executor,
        ): PokemonDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                PokemonDatabase::class.java,
                DB_NAME
            )
                // Use a separate thread for Room transactions to avoid deadlocks. This means that tests that run Room
                // transactions can't use testCoroutines.scope.runBlockingTest, and have to simply use runBlocking instead.
                .setTransactionExecutor(Executors.newSingleThreadExecutor())
                // Run queries on background I/O thread.
                .setQueryExecutor(queryExecutor)
                .build()
                .also { INSTANCE = it }
        }
    }
}