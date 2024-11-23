package com.example.chatapp.activity.call

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.launch

class CallViewModel( private val videoClient: StreamVideo): ViewModel() {
    var state by mutableStateOf(CallState(
        call = videoClient.call("default", "main-room")
    ))
        private set

    fun onAction(action: CallAction) {
        when(action) {
            CallAction.JoinCall -> {
                joinCall()
            }
            CallAction.OnDisconnectClick -> {
                state.call.leave()
                videoClient.logOut()
                state = state.copy(callState = State.ENDED)
            }
        }
    }
    private fun joinCall() {
        if(state.callState == State.ACTIVE) {
            return
        }
        viewModelScope.launch {
            state = state.copy(callState = State.JOINING)

            val shouldCreate = videoClient
                .queryCalls(filters = emptyMap())
                .getOrNull()
                ?.calls
                ?.isEmpty() == true

            state.call.join(create = shouldCreate)
                .onSuccess {
                    state = state.copy(
                        callState = State.ACTIVE,
                        error = null
                    )
                }
                .onError {
                    state = state.copy(
                        error = it.message,
                        callState = null
                    )
                }
        }
    }
}