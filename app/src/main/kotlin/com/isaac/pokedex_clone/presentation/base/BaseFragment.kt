package com.isaac.pokedex_clone.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.isaac.pokedex_clone.common.eventbus.EventBus
import javax.inject.Inject


abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
) : Fragment() {
    private var _binding: VB? = null

    protected val binding get() = _binding!!

    @Inject
    lateinit var loadingDialogManager: LoadingDialogManager

    @Inject
    lateinit var eventBus: EventBus

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflate(inflater, container, false).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeEvent()
    }

    protected abstract fun setupView()

    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected abstract fun subscribeEvent()
}

