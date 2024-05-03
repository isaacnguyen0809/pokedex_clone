package com.isaac.pokedex_clone.presentation.favourite_screen

import android.graphics.Canvas
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isaac.pokedex_clone.databinding.FragmentFavouriteBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.presentation.home_screen.viewmodel.HomeViewModel
import com.isaac.pokedex_clone.utils.OneTimeEvent
import com.isaac.pokedex_clone.utils.collectEvent
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>(FragmentFavouriteBinding::inflate) {
    private val viewModel by activityViewModels<HomeViewModel>()

    private val favouritePokemonAdapter: FavouritePokemonAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FavouritePokemonAdapter(this)
    }

    override fun setupView() {
        setupRecyclerView()
        observerData()
    }

    private fun observerData() {
        viewModel.favorStateFlow.collectIn(this) { state ->
            if (state is FavouriteUiState.Success) {
                favouritePokemonAdapter.submitList(state.data)
                binding.emptyLayout.isVisible = state.data.isEmpty()
                binding.rvPokemon.isVisible = state.data.isNotEmpty()
            } else {
                favouritePokemonAdapter.submitList(emptyList())
            }
            loadingDialogManager.showLoading(state is FavouriteUiState.Loading)
        }
        viewModel.disLikeStateFlow.collectEvent(this) {
            if (it is OneTimeEvent.Toast) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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
            adapter = favouritePokemonAdapter
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
                    val pokemon = favouritePokemonAdapter.currentList[position]
                    viewModel.dislikePokemon(pokemon)
                }

            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvPokemon)
    }

    override fun onDestroyView() {
        binding.rvPokemon.adapter = null
        super.onDestroyView()
    }
}