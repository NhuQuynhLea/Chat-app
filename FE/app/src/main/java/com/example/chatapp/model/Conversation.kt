package com.example.chatapp.model

class Conversation {
    var id = ""
    var name = ""
    var memberList = arrayListOf<User>()
    var messageList = arrayListOf<Message>()
}