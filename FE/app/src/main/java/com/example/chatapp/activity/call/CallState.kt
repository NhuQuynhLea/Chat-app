package com.example.chatapp.activity.call

import io.getstream.video.android.core.Call

data class CallState(
    val call: Call? = null,
    val callState: State? = null,
    val error: String? = null
)

enum class State {
    JOINING,
    ACTIVE,
    ENDED
}