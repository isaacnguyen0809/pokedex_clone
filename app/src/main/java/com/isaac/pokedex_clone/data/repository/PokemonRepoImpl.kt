package com.isaac.pokedex_clone.data.repository

import com.isaac.pokedex_clone.data.model.ListPokemonResponse
import com.isaac.pokedex_clone.data.remote.PokemonService
import com.isaac.pokedex_clone.domain.repository.PokemonRepository
import com.isaac.pokedex_clone.utils.AppDispatcher
import com.isaac.pokedex_clone.utils.DispatcherType
import com.isaac.pokedex_clone.utils.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepoImpl @Inject constructor(
    private val pokemonService: PokemonService,
    @AppDispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher
) : PokemonRepository {
    override suspend fun fetchPokemonList(limit: Int, offset: Int): Result<ListPokemonResponse> =
        runSuspendCatching {
            withContext(ioDispatcher) {
                pokemonService.fetchPokemonList(
                    limit = limit,
                    offset = offset,
                )
            }
        }
}