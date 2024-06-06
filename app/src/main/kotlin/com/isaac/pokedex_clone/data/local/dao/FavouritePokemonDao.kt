package com.isaac.pokedex_clone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isaac.pokedex_clone.data.local.entity.FavoritePokemonEntity

@Dao
interface FavoritePokemonDao {

    @Query("SELECT * FROM favorite_pokemon ORDER BY name DESC")
    suspend fun getAllFavoritePokemon(): List<FavoritePokemonEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPokemon(pokemon: FavoritePokemonEntity)

    @Delete
    suspend fun delete(pokemon: FavoritePokemonEntity)
}