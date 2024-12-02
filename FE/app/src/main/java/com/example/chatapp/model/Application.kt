package com.example.chatapp.model

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.define.Define
import com.example.chatapp.network.SSEClient
import com.example.chatapp.network.SSEHandler
import com.example.chatapp.storage.Storage
import com.google.android.gms.common.api.Batch
import com.launchdarkly.eventsource.MessageEvent
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltAndroidApp
class MyApplication : Application() {
    val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
    val mPreEditor = sharedPreferences.edit()
    override fun onCreate() {
        super.onCreate()
    }



    override fun onTerminate() {
        super.onTerminate()
        mPreEditor.putBoolean(Define.LOGIN_STATE,false)
        mPreEditor.apply()
    }
}