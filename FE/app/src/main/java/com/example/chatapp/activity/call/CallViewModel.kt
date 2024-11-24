package com.example.chatapp.activity.call

import android.util.Log
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

            else -> {}
        }
    }
    private fun joinCall() {
        val participants = listOf("thierry", "tommaso")
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

            val createResult = state.call.create(
                memberIds = participants,
                custom = mapOf(
                    "caller_name" to videoClient.user.name,
                    "caller_id" to videoClient.user.id
                ),
                ring = true
            )
            if(createResult.isSuccess){
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
            }else{
                Log.e("joinCall: ", "error")
            }


        }
    }
}