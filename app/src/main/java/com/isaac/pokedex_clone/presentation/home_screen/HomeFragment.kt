package com.isaac.pokedex_clone.presentation.home_screen

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.isaac.pokedex_clone.databinding.FragmentHomeBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.presentation.home_screen.viewmodel.HomeUiState
import com.isaac.pokedex_clone.presentation.home_screen.viewmodel.HomeViewModel
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val pokemonAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PokemonAdapter()
    }

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun setupView() {
        homeViewModel
        binding.rvPokemon.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = pokemonAdapter
        }
        observeData()
    }

    private fun observeData() {
        homeViewModel.uiStateFlow.collectIn(this) {
            when (it) {
                is HomeUiState.Loading -> {

                }

                is HomeUiState.Success -> {
                    pokemonAdapter.submitList(it.data)
                }

                is HomeUiState.Error -> {
                }
            }
        }
    }


}