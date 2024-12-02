package com.example.chatapp.activity.call

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.storage.Storage
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.launch

class CallViewModel(private val videoClient: StreamVideo) : ViewModel() {
    var state by mutableStateOf(
        CallState(
//        call = videoClient.call("default", )
        )
    )
        private set

    fun onAction(action: CallAction, friendId: String) {
        when (action) {
            CallAction.JoinCall -> {
                joinCall(friendId)
            }

            CallAction.OnDisconnectClick -> {
                state.call?.leave()
                videoClient.logOut()
                state = state.copy(callState = State.ENDED)
            }

            else -> {}
        }
    }



    public fun initCall(friendId: String) {
        val roomId = friendId; //Id conversation
        viewModelScope.launch {
            try {
                val call = videoClient.call("default", roomId);
                call.create();
                Log.i("JOINCALL", "Init call successful with ID: ${call.id}")
                state = state.copy(call = call)
            } catch (e: Exception) {
                Log.e("JOINCALL", "Failed to initialize call: ${e.message}")
                state = state.copy(error = e.message)
            }
        }
    }

    private fun joinCall(friendId: String) {
        if (state.callState == State.ACTIVE) {
            return
        }
        if (state.call == null) {
            initCall(friendId);
        }
        viewModelScope.launch {
            state = state.copy(callState = State.JOINING)

            val shouldCreate = videoClient
                .queryCalls(filters = emptyMap())
                .getOrNull()
                ?.calls
                ?.isEmpty() == true


            state.call!!.join(create = shouldCreate)
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
//            }else{
//                Log.e("joinCall: ", "error")
//            }


        }
    }
}