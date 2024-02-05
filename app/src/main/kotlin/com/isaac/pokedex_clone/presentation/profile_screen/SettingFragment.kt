package com.isaac.pokedex_clone.presentation.profile_screen

import androidx.fragment.app.viewModels
import com.isaac.pokedex_clone.databinding.FragmentSettingBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.presentation.login_screen.LoginViewModel
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun setupView() {
        binding.btLogout.setOnClickListener {
            viewModel.logout()
        }
        viewModel.userFlow.collectIn(viewLifecycleOwner) { userLocal ->
            binding.value.text = userLocal.takeIf { it != null }.toString()
        }
    }

}