package com.example.chatapp.model

class Conversation {
    var id = ""
    var email = ""
    var name = ""
    var memberList = arrayListOf<User>()
    var messageList = arrayListOf<String>()
}