package com.example.chatapp.model

import com.example.chatapp.storage.Storage
import java.time.LocalDateTime

class Message {
    var textContent = ""
    var sendDate = ""
    var state = ""
    var publicId= ""
    var conversationId = ""
    var type = ""
    var mediaContent = arrayListOf<String>()
    var mimeType = ""
    var senderId = ""

    fun generateTextMessage(content:String, localDateTime: LocalDateTime){
        this.textContent = content
        this.sendDate = localDateTime.toString()
        this.senderId = Storage.id
    }
}