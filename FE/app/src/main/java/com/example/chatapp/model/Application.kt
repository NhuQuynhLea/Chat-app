package com.example.chatapp.model

import android.app.Application
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.network.SSEClient
import com.example.chatapp.network.SSEHandler
import com.example.chatapp.storage.Storage
import com.launchdarkly.eventsource.MessageEvent
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }



    override fun onTerminate() {
        super.onTerminate()
    }
}