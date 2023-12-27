package com.isaac.pokedex_clone.presentation.home_screen

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.databinding.ItemPokemonBinding
import com.isaac.pokedex_clone.domain.model.Pokemon

private object PokemonDiffItemCallBack : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem == newItem
}

class PokemonAdapter :
    ListAdapter<Pokemon, PokemonAdapter.ViewHolder>(PokemonDiffItemCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), position + 1)

    inner class ViewHolder(
        private val binding: ItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pokemon, position: Int) {
            val context = binding.root.context
            binding.run {
                this.tvName.text = item.name
                this.tvNumber.text = context.getString(R.string.pokemon_number, "$position")
            }
            for (i in 1..4) {
                val chip = Chip(context)
                chip.text = "D$i"
                chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
                chip.shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(60f)
                chip.chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.transparent))
                chip.isClickable = false
                chip.setEnsureMinTouchTargetSize(false)
                binding.chipGroup.addView(chip)
            }
        }
    }
}