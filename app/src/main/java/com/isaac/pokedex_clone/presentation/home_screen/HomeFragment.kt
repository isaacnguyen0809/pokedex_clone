package com.isaac.pokedex_clone.presentation.home_screen

import androidx.recyclerview.widget.GridLayoutManager
import com.isaac.pokedex_clone.databinding.FragmentHomeBinding
import com.isaac.pokedex_clone.domain.model.Pokemon
import com.isaac.pokedex_clone.presentation.base.BaseFragment


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val pokemonAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PokemonAdapter()
    }

    override fun setupView() {
        binding.rvPokemon.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = pokemonAdapter
        }
        pokemonAdapter.submitList(
            mutableListOf(
                Pokemon(
                    name = "A", url = "asdasd", type = mutableListOf("Grass", "Poison")
                ),
                Pokemon(
                    name = "B", url = "asdasd", type = mutableListOf("Grass", "Poison")
                ),
                Pokemon(
                    name = "C", url = "asdasd", type = mutableListOf("Grass", "Poison")
                ),
                Pokemon(
                    name = "D", url = "asdasd", type = mutableListOf("Grass", "Poison")
                ),
                Pokemon(
                    name = "E", url = "asdasd", type = mutableListOf("Grass", "Poison")
                ),
                Pokemon(
                    name = "F", url = "asdasd", type = mutableListOf("Grass", "Poison")
                ),
                Pokemon(
                    name = "G", url = "asdasd", type = mutableListOf("Grass", "Poison")
                ),
                Pokemon(
                    name = "H", url = "asdasd", type = mutableListOf("Grass", "Poison")
                ),
            ),
        )
    }


}