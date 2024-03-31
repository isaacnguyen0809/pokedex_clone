package com.isaac.pokedex_clone.data.repository

import com.isaac.pokedex_clone.data.model.ListPokemonResponse
import com.isaac.pokedex_clone.data.model.PokemonInfo
import com.isaac.pokedex_clone.data.remote.PokemonService
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponse
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import com.isaac.pokedex_clone.utils.AppDispatcher
import com.isaac.pokedex_clone.utils.DispatcherType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepoImpl @Inject constructor(
    private val pokemonService: PokemonService,
    @AppDispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) : PokemonRepository {
    override suspend fun fetchPokemonList(page: Int): ApiResponse<ListPokemonResponse> =
        withContext(ioDispatcher) {
            pokemonService.fetchPokemonList(
                limit = PAGING_SIZE,
                offset = page * PAGING_SIZE,
            )
        }

    override suspend fun fetchPokemonInfo(name: String): ApiResponse<PokemonInfo> = withContext(ioDispatcher) {
        pokemonService.fetchPokemonInfo(name)
    }

    companion object {
        private const val PAGING_SIZE = 20
    }
}