package com.example.chatapp.storage

import com.example.chatapp.model.Conversation
import com.example.chatapp.model.User

object Storage {
    var userName = ""
    var password = ""
    var imageUrl = ""
    var token = ""
    var email = ""
    var id = ""
    var listConversation = arrayListOf<Conversation>()
    var listContact = arrayListOf<User>()
    var conversationChosen = Conversation()
    var messageReceive = ""
}