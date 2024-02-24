package com.isaac.pokedex_clone.presentation.login_screen

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.isaac.pokedex_clone.common.eventbus.EventType
import com.isaac.pokedex_clone.databinding.FragmentLoginBinding
import com.isaac.pokedex_clone.presentation.base.BaseFragment
import com.isaac.pokedex_clone.utils.NavigationEvent
import com.isaac.pokedex_clone.utils.collectAndRepeatFlowWithLifecycle
import com.isaac.pokedex_clone.utils.collectEvent
import com.isaac.pokedex_clone.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


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

                else -> {
                    loadingDialogManager.showLoading(false)
                }
            }
        }

        viewModel.navigationEventFlow.collectEvent(this) {
            if (it == NavigationEvent.NavigateToLogin) {
                findNavController().popBackStack()
            }
        }
    }

    override fun subscribeEvent() {
        eventBus.eventFlow.collectAndRepeatFlowWithLifecycle(
            minActiveState = Lifecycle.State.CREATED
        ) {
            if(it is EventType.EventNetworkStatus) {
                Timber.d("EVENT : $it")
            }
        }
    }

}