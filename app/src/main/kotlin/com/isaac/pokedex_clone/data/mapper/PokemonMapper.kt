package com.isaac.pokedex_clone.data.mapper

import android.os.Parcelable
import com.isaac.pokedex_clone.data.local.entity.FavouritePokemonEntity
import com.isaac.pokedex_clone.data.model.PokemonResponse
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Pokemon(
    val id: UUID,
    val name: String,
    val imgUrl: String,
    val isFavourite: Boolean = false,
) : Parcelable

fun FavouritePokemonEntity.toDomain(): Pokemon = Pokemon(
    id = UUID.fromString(id),
    name = name,
    imgUrl = imgUrl,
)

fun PokemonResponse.toDomain(): Pokemon = Pokemon(
    id = UUID.randomUUID(),
    name = name,
    imgUrl = getImageUrl(),
)

fun Pokemon.toEntity(): FavouritePokemonEntity = FavouritePokemonEntity(
    id = id.toString(),
    name = name,
    imgUrl = imgUrl,
)