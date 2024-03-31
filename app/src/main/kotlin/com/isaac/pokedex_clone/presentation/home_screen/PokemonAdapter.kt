package com.isaac.pokedex_clone.presentation.home_screen

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
import com.isaac.pokedex_clone.data.model.PokemonResponse
import com.isaac.pokedex_clone.databinding.ItemLoadingBinding
import com.isaac.pokedex_clone.databinding.ItemPokemonBinding
import com.isaac.pokedex_clone.utils.Constants
import com.isaac.pokedex_clone.utils.loadImageUrl

private object PokemonDiffItemCallBack : DiffUtil.ItemCallback<PokemonResponse>() {
    override fun areItemsTheSame(oldItem: PokemonResponse, newItem: PokemonResponse) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: PokemonResponse, newItem: PokemonResponse) = oldItem == newItem
}

class PokemonAdapter(val fragment: Fragment) :
    ListAdapter<PokemonResponse, ViewHolder>(PokemonDiffItemCallBack) {

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
        fun bind(item: PokemonResponse, position: Int) {
            val context = binding.root.context
            val extras = FragmentNavigatorExtras(binding.ivPokemon to item.getImageUrl())
            binding.ivPokemon.transitionName = item.getImageUrl()
            binding.root.setOnClickListener {
                val directions = HomeFragmentDirections.actionHomeFragmentToDetailFragment(item,position.toString())
                fragment.findNavController().navigate(
                    directions,
                    extras
                )
            }
            binding.ivPokemon.loadImageUrl(item.getImageUrl(), object : RequestListener<Drawable> {
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