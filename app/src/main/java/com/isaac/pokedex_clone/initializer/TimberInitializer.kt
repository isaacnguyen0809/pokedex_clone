package com.isaac.pokedex_clone.initializer

import android.content.Context
import androidx.startup.Initializer
import com.isaac.pokedex_clone.BuildConfig
import timber.log.Timber

@Suppress("unused")
class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(
                object : Timber.DebugTree() {
                    override fun createStackElementTag(element: StackTraceElement): String =
                        "TIMBER-(${element.fileName}:${element.lineNumber})"
                }
            )
        } else {
            TODO("Implement later remote logging Firebase Crashlytics")
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
