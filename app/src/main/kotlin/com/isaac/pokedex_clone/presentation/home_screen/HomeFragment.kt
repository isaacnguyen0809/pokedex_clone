package com.isaac.pokedex_clone.presentation.home_screen

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.isaac.pokedex_clone.databinding.FragmentHomeBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.presentation.home_screen.viewmodel.HomeUiState
import com.isaac.pokedex_clone.presentation.home_screen.viewmodel.HomeViewModel
import com.isaac.pokedex_clone.utils.collectAndRepeatFlowWithLifecycle
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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

    override fun subscribeEvent() {
        eventBus.eventFlow
            .collectAndRepeatFlowWithLifecycle(
                minActiveState = Lifecycle.State.CREATED
            ) {
                Timber.d("Network changes : $it")
            }
    }

    override fun onDestroyView() {
        binding.rvPokemon.adapter = null
        super.onDestroyView()
    }
}