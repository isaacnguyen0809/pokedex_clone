package com.isaac.pokedex_clone.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<B : ViewBinding> : BottomSheetDialogFragment() {
    private var viewBinding: B? = null
    protected val binding: B get() = viewBinding!!

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = onInflateView(inflater, container)
        viewBinding = binding
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    protected abstract fun onInflateView(inflater: LayoutInflater, container: ViewGroup?): B

    protected abstract fun setupView()

    @CallSuper
    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }
}