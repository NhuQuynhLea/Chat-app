package com.example.chatapp.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.network.SSEClient
import com.example.chatapp.network.SSEHandler
import com.example.chatapp.storage.Storage
import com.launchdarkly.eventsource.Logger
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelTest @Inject constructor(
    private val sseClient: SSEClient
) : ViewModel(), SSEHandler {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            sseClient.initSse(Storage.token, this@ViewModelTest) { error ->
                // Handle SSE connection error
                // The 'error' parameter contains the error details
            }
        }
    }

    override fun onSSEConnectionOpened() {
        TODO("Not yet implemented")
        Log.i("sse", "open");
    }

    override fun onSSEConnectionClosed() {
        TODO("Not yet implemented")
        Log.i("sse", "close");
    }

    override fun onSSEEventReceived(event: String, messageEvent: MessageEvent) {
        TODO("Not yet implemented")
        Log.i("sse", "recieve");
    }

    override fun onSSEError(t: Throwable) {
        TODO("Not yet implemented")
        Log.i("sse", "error");
    }
}