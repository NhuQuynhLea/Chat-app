package com.example.chatapp.activity.connect

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.chatapp.activity.CallActivity

class ConnectViewModel(private val app: Application):AndroidViewModel(app) {
    var state by mutableStateOf(ConnectState())
        private set

    fun onAction(action: ConnectAction) {
        when(action) {
            ConnectAction.OnConnectClick -> {
                connectToRoom()
            }
            is ConnectAction.OnNameChange -> {
                state = state.copy(name = action.name)
                Log.e( "onAction: ",action.name )
            }

            else -> {}
        }
    }

    private  fun connectToRoom() {
        state = state.copy(errorMessage = null)
        Log.e( "STATE: ", state.toString())
        if(state.name.isBlank()) {
            state = state.copy(
                errorMessage = "The username can't be blank."
            )
            return
        }

        (app as CallActivity).initVideoClient(state.name)

        state = state.copy(isConnected = true)
    }

}