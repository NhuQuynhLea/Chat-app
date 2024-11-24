package com.example.chatapp.storage

import com.example.chatapp.model.Conversation

object Storage {
    var userName = ""
    var password = ""
    var imageUrl = ""
    var token = ""
    var email = ""
    var id = ""
    var listConversation = arrayListOf<Conversation>()
    var conversationChosen = Conversation()
}