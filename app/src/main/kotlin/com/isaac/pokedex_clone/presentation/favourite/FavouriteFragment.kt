package com.isaac.pokedex_clone.presentation.favorite_screen

import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isaac.pokedex_clone.databinding.FragmentFavoriteBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.presentation.home.viewmodel.DislikedPokemonEvent
import com.isaac.pokedex_clone.presentation.home.viewmodel.HomeViewModel
import com.isaac.pokedex_clone.utils.collectEvent
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private val viewModel by activityViewModels<HomeViewModel>()

    private val favoritePokemonAdapter: FavoritePokemonAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FavoritePokemonAdapter(this)
    }

    override fun setupView() {
        setupRecyclerView()
        observerData()
    }

    private fun observerData() {
        viewModel.favorStateFlow.collectIn(this) { state ->
            if (state is FavoriteUiState.Success) {
                favoritePokemonAdapter.submitList(state.data)
                binding.emptyLayout.isVisible = state.data.isEmpty()
                binding.rvPokemon.isVisible = state.data.isNotEmpty()
            } else {
                favoritePokemonAdapter.submitList(emptyList())
            }
            loadingDialogManager.showLoading(state is FavoriteUiState.Loading)
        }
        viewModel.disLikeStateFlow.collectEvent(this) {
            if (it is DislikedPokemonEvent) {
                if (viewModel.favorStateFlow.value is FavoriteUiState.Success)
                    favoritePokemonAdapter.submitList((viewModel.favorStateFlow.value as FavoriteUiState.Success).data)
            }
        }
    }


    private fun setupRecyclerView() {
        postponeEnterTransition()
        binding.rvPokemon.doOnPreDraw {
            startPostponedEnterTransition()
        }
        binding.rvPokemon.run {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = favoritePokemonAdapter
        }
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val pokemon = favoritePokemonAdapter.currentList[position]
                    viewModel.dislikePokemon(pokemon)
                }

            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvPokemon)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListFavorite()
    }

    override fun onDestroyView() {
        binding.rvPokemon.adapter = null
        super.onDestroyView()
    }
}