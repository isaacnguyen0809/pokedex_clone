package com.isaac.pokedex_clone.fragments.home_screen

import SpacingItemDecoration
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.isaac.pokedex_clone.databinding.FragmentHomeBinding
import com.isaac.pokedex_clone.fragments.base.BaseFragment
import com.isaac.pokedex_clone.model.Pokemon


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val pokemonAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PokemonAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val navController = findNavController()
        binding.rvPokemon.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = pokemonAdapter
        }
        binding.rvPokemon.addItemDecoration(SpacingItemDecoration(50))
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