package com.isaac.pokedex_clone.presentation.base

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@MainThread
@FragmentScoped
class LoadingDialogManager @Inject constructor(
    private val fragment: Fragment
) {
    private var _loadingDialog: LoadingDialog? = null

    /*
    * Make sure dialog was destroyed when fragment or view of fragment destroyed
    in order to prevent memory leak
     */
    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner: LifecycleOwner? ->
                    viewLifecycleOwner ?: return@observe
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            _loadingDialog?.dismiss()
                            _loadingDialog = null
                        }
                    })
                }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                _loadingDialog?.dismiss()
                _loadingDialog = null
            }
        })
    }

    fun showLoading(show: Boolean) {
        val dialog = _loadingDialog ?: LoadingDialog(fragment.requireContext())
            .also {
                _loadingDialog = it
            }
        if (show) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }
}