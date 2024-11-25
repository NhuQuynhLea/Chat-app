package com.example.chatapp.network

import android.os.Build
import android.util.Log
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.internal.closeQuietly
import java.net.URI
import java.time.Duration
import java.util.concurrent.TimeUnit

class SSEClient {

    private val client = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private var sseHandlers: SSEHandler? = null
    private var eventSourceSse: EventSource? = null

    fun initSse(accessToken: String, sseHandler: SSEHandler, errorCallback: (Throwable) -> Unit) {

        this.sseHandlers = sseHandler
        val eventHandler = sseHandlers?.let { DefaultEventHandler(it) }
        val baseUrl = "https://ethical-coral-apparently.ngrok-free.app/"
        val PATH = "api/sse/subscribe" // Replace with the SSE endpoint path
        val headers = Headers.Builder()
            .add("Authorization", "Bearer $accessToken")
            .build()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                eventSourceSse = EventSource.Builder(
                    eventHandler, URI.create(baseUrl.plus(PATH))
                )
                    .headers(headers)
                    .connectTimeout(Duration.ofSeconds(60))
                    .backoffResetThreshold(Duration.ofSeconds(60))
                    .build()
                eventSourceSse?.let {
                    it.start()
                }

            }
        } catch (e: Exception) {
            errorCallback(e)
        }
    }

    class DefaultEventHandler(private val sseHandler: SSEHandler) : EventHandler {

        override fun onOpen() {
            sseHandler.onSSEConnectionOpened()
        }

        override fun onClosed() {
            sseHandler.onSSEConnectionClosed()
        }

        override fun onMessage(event: String, messageEvent: MessageEvent) {
            when(event) {
                "new-message" -> {
                    println("New Message Event Received: ${messageEvent.data}")
                }
            }
            sseHandler.onSSEEventReceived(event,messageEvent)
        }

        override fun onError(t: Throwable) {
            sseHandler.onSSEError(t)
        }

        override fun onComment(comment: String) {
            Log.i("SSE_CONNECTION", comment)
        }
    }

    fun disconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                eventSourceSse?.let {
                    it.closeQuietly()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}