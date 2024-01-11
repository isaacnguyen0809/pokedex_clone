package com.isaac.pokedex_clone.presentation.home_screen

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
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
import com.isaac.pokedex_clone.data.model.PokemonResponse
import com.isaac.pokedex_clone.databinding.ItemPokemonBinding
import com.isaac.pokedex_clone.utils.loadImageUrl

private object PokemonDiffItemCallBack : DiffUtil.ItemCallback<PokemonResponse>() {
    override fun areItemsTheSame(oldItem: PokemonResponse, newItem: PokemonResponse) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: PokemonResponse, newItem: PokemonResponse) = oldItem == newItem
}

class PokemonAdapter(val fragment: Fragment) :
    ListAdapter<PokemonResponse, PokemonAdapter.ViewHolder>(PokemonDiffItemCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position + 1)
    }

    inner class ViewHolder(
        private val binding: ItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PokemonResponse, position: Int) {
            val context = binding.root.context
            binding.root.setOnClickListener {
                fragment.findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
            }
            binding.imageView.loadImageUrl(item.getImageUrl(), object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean = false

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
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