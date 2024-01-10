package com.isaac.pokedex_clone.presentation.home_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.data.model.PokemonResponse
import com.isaac.pokedex_clone.databinding.ItemPokemonBinding

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
            Glide.with(context)
                .load(item.getImageUrl())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(binding.imageView)
            binding.run {
                this.tvName.text = item.name
                this.tvNumber.text = context.getString(R.string.pokemon_number, "$position")
            }
        }
    }


}