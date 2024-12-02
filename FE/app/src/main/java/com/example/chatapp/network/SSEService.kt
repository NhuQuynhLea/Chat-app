package com.example.chatapp.network

import android.Manifest
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.chatapp.R
import com.example.chatapp.activity.ChatActivity
import com.example.chatapp.model.MyApplication
import com.example.chatapp.storage.Storage
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.random.Random

class SSEService :IntentService(SSEService::class.java.simpleName){
    private lateinit var sseClient: SSEClient
    val CHANNEL_ID = "simple_notification_channel"
    val CHANNEL_NAME = "Simple Notification Channel"

    override fun onHandleIntent(intent: Intent?){
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        CoroutineScope(Dispatchers.IO).launch  {
            sseClient = SSEClient()
            startSSE()
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSSE()
    }

    private fun startSSE() {
        sseClient.initSse(
            Storage.token,
            sseHandler = object : SSEHandler {
                override fun onSSEConnectionOpened() {
                    updateStatus("SSE Connection Opened")
                }

                override fun onSSEConnectionClosed() {
                    updateStatus("SSE Connection Closed")
                }

                override fun onSSEEventReceived(event: String, messageEvent: MessageEvent) {
                    updateStatus("Received Event: $event with Message: ${messageEvent.data}")
                    if (messageEvent.data.contains('{')){
                        if (messageEvent.data.contains("textContent")){
                            val message = API.readMessageFromJson(JSONObject(messageEvent.data))
                            if (!message.senderId.equals(Storage.id)){
                                CoroutineScope(Dispatchers.IO).launch {
                                    val conversation = API.getOneConversation(this@SSEService,message.conversationId,Storage.token)
                                    val builder = NotificationCompat.Builder(this@SSEService, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.app_icon)
                                        .setContentTitle(conversation.name)
                                        .setContentText(message.textContent)
                                        .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                                    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(Random.nextInt(), builder.build())
                                }
                            }
                        }
                        Storage.messageReceive = messageEvent.data
                    }

                }

                override fun onSSEError(t: Throwable) {
                    updateStatus("Error occurred in SSE connection "+t.message.toString())
                }
            },
            errorCallback = { throwable ->
                updateStatus("Error initializing SSE: ${throwable.message}")
            }
        )
    }


    private fun stopSSE() {
        // Ensure to disconnect the SSE client when the activity stops
        CoroutineScope(Dispatchers.IO).launch {
            try {
                sseClient.disconnect()
                withContext(Dispatchers.Main) {
                    updateStatus("SSE Disconnected")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateStatus(status: String) {
        Log.e("update",status)
    }
}

