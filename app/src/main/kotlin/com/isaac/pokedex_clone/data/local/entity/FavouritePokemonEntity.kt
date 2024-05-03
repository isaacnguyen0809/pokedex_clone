package com.isaac.pokedex_clone.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_pokemon")
data class FavouritePokemonEntity(
    @ColumnInfo(name = "id")
    val id: String,
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "imgUrl")
    val imgUrl: String,
)