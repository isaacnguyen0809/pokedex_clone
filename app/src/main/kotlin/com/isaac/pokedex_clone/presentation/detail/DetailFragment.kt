package com.isaac.pokedex_clone.presentation.detail

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.transition.TransitionInflater
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.data.model.PokemonInfo
import com.isaac.pokedex_clone.databinding.FragmentDetailBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.utils.OneTimeEvent
import com.isaac.pokedex_clone.utils.PokemonTypeUtils
import com.isaac.pokedex_clone.utils.collectEventChannel
import com.isaac.pokedex_clone.utils.collectIn
import com.isaac.pokedex_clone.utils.loadImageUrl
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val viewModel by viewModels<DetailViewModel>()

    private val args by navArgs<DetailFragmentArgs>()

    private val window by lazy {
        this.requireActivity().window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    private val animCustom by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            androidx.transition.R.anim.fragment_open_enter
        ).apply {
            duration = 1000
        }
    }

    override fun setupView() {
        setUpShareElementTransition()
        viewModel.getPokemonInfo(args.itemPokemon.name)
        binding.tvIndex.text = getString(R.string.pokemon_number, args.position)
        binding.tvName.text = args.itemPokemon.name.replaceFirstChar { it.uppercase() }

        binding.ivPokemon.transitionName = args.itemPokemon.imgUrl
        binding.ivPokemon.loadImageUrl(args.itemPokemon.imgUrl, object : RequestListener<Drawable> {
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
                        window.statusBarColor = startColor
                        binding.lpbHp.setIndicatorColor(endColor)
                        binding.lpbAtk.setIndicatorColor(endColor)
                        binding.lpbDef.setIndicatorColor(endColor)
                        binding.lpbSpd.setIndicatorColor(endColor)
                        binding.lpbExp.setIndicatorColor(endColor)
                        binding.sivHeader.setBackgroundDrawable(GradientDrawable().apply {
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
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.mauvelous)
        }


        binding.ivFavorite.setOnClickListener {
            if (viewModel.uiState.value is DetailUiState.Success) {
                val pokemonInfo = (viewModel.uiState.value as DetailUiState.Success).pokemonInfo
                val pokemon = args.itemPokemon
                if (pokemonInfo.isFavorited) {
                    viewModel.dislikePokemon(pokemon)
                } else {
                    viewModel.likePokemon(pokemon)
                }
            }
        }


        viewModel.uiState.collectIn(this) { state ->
            binding.pbLoading.isVisible = state is DetailUiState.Loading
            binding.btRetry.isVisible = state is DetailUiState.Error
            binding.btRetry.setOnClickListener {
                viewModel.getPokemonInfo(args.itemPokemon.name)
            }
            binding.clContent.isVisible = state is DetailUiState.Success
            when (state) {
                is DetailUiState.Success -> {

                    binding.clContent.startAnimation(animCustom)
                    state.pokemonInfo.types.forEach {
                        addChipItem(
                            text = it.type.name,
                            PokemonTypeUtils.getTypeColor(it.type.name)
                        )
                    }
                    binding.tvHeightValue.text = state.pokemonInfo.getHeightString()
                    binding.tvWeightValue.text = state.pokemonInfo.getWeightString()

                    binding.tvValueHp.text = state.pokemonInfo.getHpString()
                    binding.lpbHp.max = PokemonInfo.MAX_HP
                    binding.lpbHp.progress = state.pokemonInfo.hp

                    binding.tvAtkValue.text = state.pokemonInfo.getAttackString()
                    binding.lpbAtk.max = PokemonInfo.MAX_ATTACK
                    binding.lpbAtk.progress = state.pokemonInfo.attack

                    binding.tvDefValue.text = state.pokemonInfo.getDefenseString()
                    binding.lpbDef.max = PokemonInfo.MAX_DEFENSE
                    binding.lpbDef.progress = state.pokemonInfo.defense

                    binding.tvSpdValue.text = state.pokemonInfo.getSpeedString()
                    binding.lpbSpd.max = PokemonInfo.MAX_SPEED
                    binding.lpbSpd.progress = state.pokemonInfo.speed

                    binding.tvExpValue.text = state.pokemonInfo.getExpString()
                    binding.lpbExp.max = PokemonInfo.MAX_EXP
                    binding.lpbExp.progress = state.pokemonInfo.exp
                    val colorIvFavorite =
                        if (state.pokemonInfo.isFavorited) R.color.fire else R.color.white
                    binding.ivFavorite.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            colorIvFavorite
                        )
                    )
                }

                else -> {
                    binding.ivFavorite.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )

                }
            }
        }

        viewModel.eventChannel.collectEventChannel(this) {
            if (it is OneTimeEvent.Toast) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpShareElementTransition() {
        val animation = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                duration = 500
            }
        sharedElementEnterTransition = animation
    }

    private fun addChipItem(text: String = "", backgroundColor: Int = R.color.mauvelous) {
        binding.cgElement.removeAllViews()
        val chip = Chip(this.requireContext())
        chip.text = text.replaceFirstChar { it.uppercase() }
        chip.isEnabled = false
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        chip.chipStrokeWidth = 0f
        chip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), backgroundColor))
        chip.shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(30f)
        chip.setEnsureMinTouchTargetSize(false)
        binding.cgElement.addView(chip)
    }

}