package com.isaac.pokedex_clone.presentation.home_screen

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.isaac.pokedex_clone.databinding.FragmentHomeBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.presentation.home_screen.viewmodel.HomeUiState
import com.isaac.pokedex_clone.presentation.home_screen.viewmodel.HomeViewModel
import com.isaac.pokedex_clone.utils.OneTimeEvent
import com.isaac.pokedex_clone.utils.collectEvent
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val pokemonAdapter: PokemonAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PokemonAdapter(this)
    }

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun setupView() {
        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        binding.rvPokemon.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = pokemonAdapter
        }
    }

    private fun observeData() {
        homeViewModel.uiStateFlow.collectIn(this) {
            if (it is HomeUiState.Success) {
                pokemonAdapter.submitList(it.data)
            }
            loadingDialogManager.showLoading(it is HomeUiState.Loading)
        }
        homeViewModel.errorEventFlow.collectEvent(this) {
            if (it is OneTimeEvent.Toast) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        binding.rvPokemon.adapter = null
        super.onDestroyView()
    }
}