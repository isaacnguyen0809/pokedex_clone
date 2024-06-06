package com.isaac.pokedex_clone.presentation.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.data.mapper.Pokemon
import com.isaac.pokedex_clone.databinding.ItemLoadingBinding
import com.isaac.pokedex_clone.databinding.ItemPokemonBinding
import com.isaac.pokedex_clone.utils.Constants
import com.isaac.pokedex_clone.utils.loadImageUrl

private object PokemonDiffItemCallBack : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem == newItem
}

class PokemonAdapter(val fragment: Fragment, val onLikedPokemon: (Pokemon) -> Unit, val onDislikePokemon: (Pokemon) -> Unit) :
    ListAdapter<Pokemon, ViewHolder>(PokemonDiffItemCallBack) {

    override fun getItemViewType(position: Int): Int {
        return if (this.currentList[position] == null) {
            Constants.VIEW_ITEM_LOADING
        } else {
            Constants.VIEW_ITEM_TYPE
        }
    }

    fun addShowLoadingView() {
        submitList(currentList + null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == Constants.VIEW_ITEM_TYPE) {
            ItemViewHolder(ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            LoadingViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(getItem(position), position + 1)
        }
    }

    inner class LoadingViewHolder(
        binding: ItemLoadingBinding,
    ) : ViewHolder(binding.root)

    inner class ItemViewHolder(
        private val binding: ItemPokemonBinding,
    ) : ViewHolder(binding.root) {
        fun bind(item: Pokemon, position: Int) {
            val context = binding.root.context
            val extras = FragmentNavigatorExtras(binding.ivPokemon to item.imgUrl)
            binding.ivPokemon.transitionName = item.imgUrl
            binding.ivFavorite.setOnClickListener {
                if (item.isFavorite) {
                    onDislikePokemon.invoke(item)
                } else {
                    onLikedPokemon.invoke(item)
                }
            }
            val colorIvFavorite = if (item.isFavorite) R.color.fire else R.color.white
            binding.ivFavorite.setColorFilter(context.getColor(colorIvFavorite))
            binding.root.setOnClickListener {
                val directions = HomeFragmentDirections.actionHomeFragmentToDetailFragment(item, position.toString())
                fragment.findNavController().navigate(
                    directions,
                    extras
                )
            }
            binding.ivPokemon.loadImageUrl(item.imgUrl, object : RequestListener<Drawable> {
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
                        palette?.lightMutedSwatch?.rgb?.let {
                            binding.materialCardView.setCardBackgroundColor(
                                it
                            )
                        }
                    }
                    return false
                }
            })

            binding.run {
                this.tvName.text = item.name.replaceFirstChar { it.uppercase() }
                this.tvNumber.text = context.getString(R.string.pokemon_number, "$position")
            }
        }
    }

}