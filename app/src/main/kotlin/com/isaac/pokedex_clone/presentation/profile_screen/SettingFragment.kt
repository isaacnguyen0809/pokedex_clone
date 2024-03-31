package com.isaac.pokedex_clone.presentation.profile_screen

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.databinding.FragmentSettingBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.presentation.login_screen.LoginViewModel
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun setupView() {
        binding.btLogout.setOnClickListener {
            viewModel.logout()
        }
        binding.btLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        viewModel.userFlow.collectIn(this) { userLocal ->
            binding.loginValue.text = userLocal?.toString() ?: ""

            binding.btLogout.isVisible = userLocal != null
            binding.btLogin.isVisible = userLocal == null
        }

        binding.btDemo.setOnClickListener {
            viewModel.callDemo()
        }

        viewModel.demoStateFlow.collectIn(this) {
            binding.demoValue.text = it
        }

        loginViewModel.userFlow.collectIn(this) {
            binding.btDemo.isVisible = it != null
            binding.demoValue.isVisible = it != null
        }
    }

}