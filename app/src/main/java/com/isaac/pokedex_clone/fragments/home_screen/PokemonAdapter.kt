package com.isaac.pokedex_clone.fragments.home_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.databinding.ItemPokemonBinding
import com.isaac.pokedex_clone.model.Pokemon

private object PokemonDiffItemCallBack : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem == newItem
}

class PokemonAdapter(private val context: Context) :
    ListAdapter<Pokemon, PokemonAdapter.ViewHolder>(PokemonDiffItemCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), position + 1)

    inner class ViewHolder(
        private val binding: ItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pokemon, position: Int) = binding.run {
            this.tvName.text = item.name
            this.tvNumber.text = context.getString(R.string.pokemon_number, "$position")
        }
    }
}