package com.example.chatapp.activity

import android.app.Application
import com.example.chatapp.activity.appModule
import io.getstream.android.push.firebase.FirebasePushDeviceGenerator
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.notifications.NotificationConfig
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CallActivity: Application() {
    private var currentName: String? = null
    var client: StreamVideo? = null

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CallActivity)
            modules(appModule)
        }
    }

    fun initVideoClient(username: String) {
        val notificationConfig = NotificationConfig(
            pushDeviceGenerators = listOf(FirebasePushDeviceGenerator(providerName = "firebase"))
        )

        if(client == null || username != currentName) {
            StreamVideo.removeClient()
            currentName =  username
            client = StreamVideoBuilder(
                context = this,
                apiKey = "cptuxtqg7and",
                user = User(
                    id = "username",
                    name = username,
                    type = UserType.Guest
                ),
                notificationConfig = notificationConfig,
            ).build()
        }
    }

}