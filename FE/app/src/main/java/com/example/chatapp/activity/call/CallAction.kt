package com.example.chatapp.activity.call

sealed interface CallAction{
    data object OnDisconnectClick: CallAction
    data object JoinCall: CallAction

}