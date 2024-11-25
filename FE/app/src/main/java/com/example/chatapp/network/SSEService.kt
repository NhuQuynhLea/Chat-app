package com.example.chatapp.network

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.chatapp.storage.Storage
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SSEService :IntentService(SSEService::class.java.simpleName){
    private lateinit var sseClient: SSEClient
    override fun onHandleIntent(intent: Intent?){
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

