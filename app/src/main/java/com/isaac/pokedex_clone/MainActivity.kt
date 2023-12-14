package com.isaac.pokedex_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.isaac.pokedex_clone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavigationController()
    }

    private fun setupNavigationController() {
    }
}