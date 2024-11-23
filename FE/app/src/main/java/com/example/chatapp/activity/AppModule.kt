package com.example.chatapp.activity

import com.example.chatapp.activity.call.CallViewModel
import com.example.chatapp.activity.connect.ConnectViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    factory {
        val app = androidContext().applicationContext as CallActivity
        app.client
    }

    viewModelOf(::ConnectViewModel)
    viewModelOf(::CallViewModel)
}