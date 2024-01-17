package com.isaac.pokedex_clone.presentation.login_screen

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.databinding.FragmentLoginBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun setupView() {
        binding.btLogin.setOnClickListener {
            viewModel.login()
        }

        viewModel.uiStateFlow.collectIn(this) {
            when (it) {
                is LoginUiState.Error -> {
                    loadingDialogManager.showLoading(false)
                    Toast.makeText(requireContext(), "${it.error}", Toast.LENGTH_SHORT).show()
                }

                is LoginUiState.Loading -> {
                    loadingDialogManager.showLoading(true)
                }

                is LoginUiState.Success -> {
                    delay(3000)
                    loadingDialogManager.showLoading(false)
                    findNavController().navigate(R.id.action_loginFragment_to_home_nav)
                }

                else -> {
                    loadingDialogManager.showLoading(false)
                }
            }
        }
    }

}