package com.isaac.pokedex_clone.presentation.detail_screen

import androidx.navigation.fragment.findNavController
import com.isaac.pokedex_clone.databinding.FragmentDetailBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    override fun setupView() {
        binding.btBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}