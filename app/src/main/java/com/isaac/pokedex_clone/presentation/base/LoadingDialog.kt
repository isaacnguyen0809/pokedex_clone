package com.isaac.pokedex_clone.presentation.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.databinding.LoadingDialogBinding

class LoadingDialog(context: Context) : Dialog(context) {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        LoadingDialogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(R.color.transparent)
        setCancelable(false)
    }
}