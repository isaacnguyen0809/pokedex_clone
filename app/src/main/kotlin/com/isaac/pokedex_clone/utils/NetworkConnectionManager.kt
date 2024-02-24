package com.isaac.pokedex_clone.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.isaac.pokedex_clone.common.eventbus.EventBus
import com.isaac.pokedex_clone.common.eventbus.EventType
import timber.log.Timber

class NetworkCallback(private val eventBus: EventBus) : ConnectivityManager.NetworkCallback() {

    override fun onLost(network: Network) {
        Timber.d("onLost network: $network")
        eventBus.produceEvent(EventType.EventNetworkStatus(isOnline = false))
    }

    override fun onAvailable(network: Network) {
        Timber.d("onAvailable: network: $network")
        eventBus.produceEvent(EventType.EventNetworkStatus(isOnline = true))
    }

}

class NetworkConnectionManager(val context: Context, eventBus: EventBus) {

    private val callback: NetworkCallback = NetworkCallback(eventBus)

    private val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private val request = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
        .build()

    fun registerCallback() {
        connectivityManager.registerNetworkCallback(request, callback)
    }

    fun unregisterCallback() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

}