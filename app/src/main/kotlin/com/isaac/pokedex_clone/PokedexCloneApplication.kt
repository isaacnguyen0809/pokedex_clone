package com.isaac.pokedex_clone

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokedexCloneApplication : Application() {
    companion object {
        lateinit var gInstance: PokedexCloneApplication
    }

    override fun onCreate() {
        super.onCreate()
        gInstance = this@PokedexCloneApplication
    }
}