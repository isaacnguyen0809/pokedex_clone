package com.isaac.pokedex_clone.presentation.home_screen

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.databinding.FragmentHomeBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.presentation.home_screen.viewmodel.HomeUiState
import com.isaac.pokedex_clone.presentation.home_screen.viewmodel.HomeViewModel
import com.isaac.pokedex_clone.utils.Constants
import com.isaac.pokedex_clone.utils.OneTimeEvent
import com.isaac.pokedex_clone.utils.collectEvent
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val pokemonAdapter: PokemonAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PokemonAdapter(this)
    }

    private val viewModel by viewModels<HomeViewModel>()

    override fun setupView() {

        setupRecyclerView()
        observeData()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getListPokemon(true)
        }
        val refreshColor = ContextCompat.getColor(requireContext(), R.color.mauvelous)
        binding.swipeRefreshLayout.setColorSchemeColors(refreshColor)
    }

    private fun setupRecyclerView() {
        postponeEnterTransition()
        binding.rvPokemon.doOnPreDraw {
            startPostponedEnterTransition()
        }
        binding.rvPokemon.run {
            layoutManager = GridLayoutManager(context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (pokemonAdapter.getItemViewType(position)) {
                            Constants.VIEW_ITEM_TYPE -> 1
                            Constants.VIEW_ITEM_LOADING -> 2
                            else -> -1
                        }
                    }
                }
            }
            adapter = pokemonAdapter
        }
        val gridLayoutManager = binding.rvPokemon.layoutManager as GridLayoutManager

        binding.rvPokemon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (
                    dy > 0 &&
                    gridLayoutManager.findLastVisibleItemPosition() + VISIBLE_THRESHOLD >= gridLayoutManager.itemCount
                ) {
                    viewModel.loadNextPage {
                        pokemonAdapter.addShowLoadingView()
                    }
                }
            }
        })
    }

    private fun observeData() {
        viewModel.uiStateFlow.collectIn(this) { state ->
            if (state is HomeUiState.Success) {
                pokemonAdapter.submitList(state.data)
            } else {
                pokemonAdapter.submitList(emptyList())
            }
            binding.swipeRefreshLayout.isRefreshing = false
            loadingDialogManager.showLoading(state is HomeUiState.Loading)

        }
        viewModel.errorEventFlow.collectEvent(this) {
            if (it is OneTimeEvent.Toast) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        binding.rvPokemon.adapter = null
        super.onDestroyView()
    }

    companion object {
        private const val VISIBLE_THRESHOLD = 1
    }
}