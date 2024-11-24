package com.example.chatapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.ringing.RingingCallContent
import io.getstream.video.android.core.call.state.CallAction
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.core.call.state.ToggleSpeakerphone

class IncomingCallActivity:  ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//            VideoTheme {
//                RingingCallContent(
//                    modifier = Modifier.background(color = VideoTheme.colors),
//                    call = call,
//                    onBackPressed = { finish() },
//                    onAcceptedContent = {
//                        CallContent(
//                            modifier = Modifier.fillMaxSize(),
//                            call = call,
//                            onCallAction = onCallAction
//                        )
//                    },
//                    onCallAction = onCallAction
//                )
//            }
//        }
//    }
//
//    val onCallAction: (CallAction) -> Unit = { callAction ->
//        when (callAction) {
//            is ToggleCamera -> call.camera.setEnabled(callAction.isEnabled)
//            is ToggleMicrophone -> call.microphone.setEnabled(callAction.isEnabled)
//            is ToggleSpeakerphone -> call.speaker.setEnabled(callAction.isEnabled)
//            is LeaveCall -> finish()
//            else -> Unit
//        }
    }

}