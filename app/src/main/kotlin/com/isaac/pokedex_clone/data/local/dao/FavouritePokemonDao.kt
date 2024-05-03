package com.isaac.pokedex_clone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isaac.pokedex_clone.data.local.entity.FavouritePokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritePokemonDao {

    @Query("SELECT * FROM favourite_pokemon ORDER BY name DESC")
    suspend fun getAllFavouritePokemon(): List<FavouritePokemonEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPokemon(pokemon: FavouritePokemonEntity)

    @Delete
    suspend fun delete(pokemon: FavouritePokemonEntity)
}