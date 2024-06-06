package com.isaac.pokedex_clone.presentation.favorite_screen

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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.data.mapper.Pokemon
import com.isaac.pokedex_clone.databinding.ItemFavoritePokemonBinding
import com.isaac.pokedex_clone.utils.loadImageUrl

private object FavoritePokemonDiffItemCallBack : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem == newItem
}

class FavoritePokemonAdapter(val fragment: Fragment) :
    ListAdapter<Pokemon, FavoritePokemonAdapter.ItemViewHolder>(FavoritePokemonDiffItemCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =

        ItemViewHolder(ItemFavoritePokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), position + 1)
    }


    inner class ItemViewHolder(
        private val binding: ItemFavoritePokemonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pokemon, position: Int) {
            val context = binding.root.context
            val extras = FragmentNavigatorExtras(binding.ivPokemon to item.imgUrl)
            binding.ivPokemon.transitionName = item.imgUrl
            binding.root.setOnClickListener {
                val directions = FavoriteFragmentDirections.actionFavFragmentToDetailFragment(
                    item, position.toString()
                )
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