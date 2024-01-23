package com.isaac.pokedex_clone.presentation.home_screen

import android.widget.Toast
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

    private lateinit var pokemonAdapter: PokemonAdapter

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun setupView() {
        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        pokemonAdapter = PokemonAdapter(this)
        binding.rvPokemon.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = pokemonAdapter
        }
    }

    private fun observeData() {
        homeViewModel.uiStateFlow.collectIn(this) {
            when (it) {
                is HomeUiState.Loading -> {
                    loadingDialogManager.showLoading(true)
                }

                is HomeUiState.Success -> {
                    loadingDialogManager.showLoading(false)
                    pokemonAdapter.submitList(it.data)
                }

                is HomeUiState.Error -> {
                    loadingDialogManager.showLoading(false)
                    Toast.makeText(requireContext(), "${it.error}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}