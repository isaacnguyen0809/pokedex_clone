package com.isaac.pokedex_clone.presentation.detail_screen

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.transition.TransitionInflater
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.databinding.FragmentDetailBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.utils.collectIn
import com.isaac.pokedex_clone.utils.loadImageUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {


    private val viewModel by viewModels<DetailViewModel>()

    private val args by navArgs<DetailFragmentArgs>()

    override fun setupView() {

        val animation = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                duration = 500
            }
        sharedElementEnterTransition = animation
        binding.index.text = getString(R.string.pokemon_number, args.position)
        binding.tvName.text = args.itemPokemon.name.replaceFirstChar { it.uppercase() }
        binding.ivPokemon.transitionName = args.itemPokemon.getImageUrl()
        binding.ivPokemon.loadImageUrl(args.itemPokemon.getImageUrl(), object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean,
            ): Boolean = false

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean,
            ): Boolean {
                Palette.from(resource.toBitmap()).generate { palette ->
                    val startColor = palette?.lightMutedSwatch?.rgb
                    val endColor = palette?.dominantSwatch?.rgb

                    if (startColor != null && endColor != null) {
                        binding.header.setBackgroundDrawable(GradientDrawable().apply {
                            colors = intArrayOf(
                                startColor,
                                endColor
                            )
                            orientation = GradientDrawable.Orientation.TOP_BOTTOM
                            gradientType = GradientDrawable.LINEAR_GRADIENT
                            shape = GradientDrawable.RECTANGLE
                        })
                    }
                }
                return false
            }
        })

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.uiState.collectIn(this) { state ->
//            binding.progressBar.isVisible = state is DetailUiState.Loading
            when (state) {
                is DetailUiState.Success -> {
//                    binding.tvResult.text = state.pokemonInfo.toString()
                }

                is DetailUiState.Error -> {
//                    binding.tvResult.text = state.error
                }

                else -> {

                }
            }
        }
    }

}