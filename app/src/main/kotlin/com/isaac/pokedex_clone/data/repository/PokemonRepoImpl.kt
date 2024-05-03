package com.isaac.pokedex_clone.data.repository

import com.isaac.pokedex_clone.data.local.dao.FavouritePokemonDao
import com.isaac.pokedex_clone.data.mapper.Pokemon
import com.isaac.pokedex_clone.data.mapper.toDomain
import com.isaac.pokedex_clone.data.mapper.toEntity
import com.isaac.pokedex_clone.data.model.ListPokemonResponse
import com.isaac.pokedex_clone.data.model.PokemonInfo
import com.isaac.pokedex_clone.data.remote.PokemonService
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import com.isaac.pokedex_clone.utils.AppDispatcher
import com.isaac.pokedex_clone.utils.DispatcherType
import com.isaac.pokedex_clone.utils.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepoImpl @Inject constructor(
    private val pokemonService: PokemonService,
    private val favouritePokemonDao: FavouritePokemonDao,
    @AppDispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) : PokemonRepository {
    override suspend fun fetchPokemonList(page: Int): ApiResponse<ListPokemonResponse> = withContext(ioDispatcher) {
        pokemonService.fetchPokemonList(
            limit = PAGING_SIZE,
            offset = page * PAGING_SIZE,
        )
    }

    override suspend fun fetchPokemonInfo(name: String): ApiResponse<PokemonInfo> = withContext(ioDispatcher) {
        pokemonService.fetchPokemonInfo(name)
    }

    override suspend fun likePokemon(pokemon: Pokemon): Result<Unit> = runSuspendCatching {
        withContext(ioDispatcher) {
            favouritePokemonDao.insertPokemon(pokemon.toEntity())
        }
    }

    override suspend fun unlikePokemon(pokemon: Pokemon): Result<Unit> = runSuspendCatching {
        withContext(ioDispatcher) {
            favouritePokemonDao.delete(pokemon.toEntity())
        }
    }


    override suspend fun getAllFavouritePokemon(): Result<List<Pokemon>> = runSuspendCatching {
        withContext(ioDispatcher) {
            favouritePokemonDao.getAllFavouritePokemon().map { it.toDomain() }
        }
    }

    companion object {
        private const val PAGING_SIZE = 20
    }
}