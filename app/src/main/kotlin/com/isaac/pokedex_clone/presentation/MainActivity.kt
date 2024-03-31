package com.isaac.pokedex_clone.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.isaac.pokedex_clone.R
import com.isaac.pokedex_clone.databinding.ActivityMainBinding
import com.isaac.pokedex_clone.utils.NetworkMonitor
import com.isaac.pokedex_clone.utils.collectIn
import com.isaac.pokedex_clone.utils.triggerAnim
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavigationController()
        observeNetworkConnection()
    }

    private fun observeNetworkConnection() {
        networkMonitor.isOnline.collectIn(this) {
            binding.cardViewInternet.triggerAnim(isShow = it.not())
        }
    }

    private fun setupNavigationController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frgContainer) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Timber.d("Destination: ${destination}")
            when(destination.id){
                R.id.loginFragment -> {
                    binding.bottomNavigationView.isVisible = false
                }
                R.id.detailFragment -> {
                    binding.bottomNavigationView.isVisible = false
                }
                else -> {
                    binding.bottomNavigationView.isVisible = true
                }
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)
    }

}