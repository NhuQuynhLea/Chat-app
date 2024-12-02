package com.example.chatapp.component.mainComponents

import android.app.Activity
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.activity.ChatActivity
import com.example.chatapp.model.Conversation
import com.example.chatapp.network.API
import com.example.chatapp.storage.CustomFont
import com.example.chatapp.storage.Storage
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MessagesComponent() {
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val conversationList = remember {
        mutableStateListOf<Conversation>()
    }
    val today = LocalDateTime.now()
    Storage.listConversation.forEach { conversationList.add(it) }
    LaunchedEffect(key1 = Unit) {
        while (true){
            if (Storage.messageReceive.isNotEmpty()){
                Log.e("check",Storage.messageReceive.contains("textContent").toString())
                if (Storage.messageReceive.contains("textContent")){
                    val message = API.readMessageFromJson(JSONObject(Storage.messageReceive))
                    for (i in 0..<conversationList.size){
                        if (message.conversationId.equals(conversationList[i].id)){
                            val conversation = conversationList[i]
                            conversationList.removeAt(i)
                            conversation.messageList.add(message)
                            conversationList.add(conversation)
                            break
                        }
                    }
                }else {
                    CoroutineScope(Dispatchers.IO).launch {
                        Storage.listConversation = API.getAllConversation(context = context, Storage.token)
                        conversationList.clear()
                        Storage.listConversation.forEach { conversationList.add(it) }
                    }
                }
                Storage.messageReceive=""
            }
            delay(500)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .background(Color.Transparent)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                shape = RoundedCornerShape(10.dp),
                placeholder = {
                    if (searchText.isEmpty()) Text(text = "Tìm kiếm", color = Color.Gray, fontFamily = CustomFont.font)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty())
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                searchText=""
                                focusManager.clearFocus()
                            }
                        )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyColumn() {
            items(conversationList.reversed()) { conversation ->
                var conversationName = ""
                if (conversation.memberList.size>2) conversationName=conversation.name
                else {
                    conversation.name.split("/").forEach{
                        if (!it.equals(Storage.userName)){
                            conversationName = it
                        }
                    }
                }
                if (conversationName.lowercase().contains(searchText.trim())){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clickable {
                                CoroutineScope(Dispatchers.IO).launch {
                                    API.markAsRead(context = context, id = conversation.id, token = Storage.token)
                                    Storage.conversationChosen = API.getOneConversation(
                                        context = context,
                                        id = conversation.id,
                                        token = Storage.token
                                    )
                                    Storage.conversationChosen.messageList.sortBy { it.sendDate }
                                    if(conversation.messageList.isNotEmpty()) conversation.messageList.last().state = "READ"
                                    val intent = Intent(context, ChatActivity::class.java)
                                    context.startActivity(intent)
                                }
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                //Avatar
                                Column(modifier = Modifier.weight(0.2f)) {
                                    Box(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .padding(10.dp)
                                    ) {
                                        if (conversation.memberList.size == 2) {
                                            Image(
                                                painter = painterResource(id = R.drawable.avatar),
                                                contentDescription = "",
                                                contentScale = ContentScale.FillBounds,
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                                    .clip(
                                                        RoundedCornerShape(100.dp)
                                                    )
                                            )
                                        } else {
                                            Image(
                                                painter = painterResource(id = R.drawable.avatar),
                                                contentDescription = "",
                                                contentScale = ContentScale.FillBounds,
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .fillMaxSize(0.7f)
                                                    .clip(
                                                        RoundedCornerShape(100.dp)
                                                    )
                                            )
                                            Image(
                                                painter = painterResource(id = R.drawable.avatar),
                                                contentDescription = "",
                                                contentScale = ContentScale.FillBounds,
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .fillMaxSize(0.7f)
                                                    .clip(
                                                        RoundedCornerShape(100.dp)
                                                    )
                                                    .border(
                                                        width = 2.dp,
                                                        shape = RoundedCornerShape(100.dp),
                                                        color = Color.White
                                                    )
                                            )
                                        }

                                    }

                                }

                                //Text
                                Column(
                                    modifier = Modifier
                                        .weight(0.6f)
                                        .padding(vertical = 20.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.weight(0.5f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = conversationName,
                                            color = colorResource(id = R.color.purple_700),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = CustomFont.font
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.weight(0.5f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
//                                        Text(
//                                            text = "Bản nháp: ",
//                                            color = colorResource(id = R.color.mainColor),
//                                            fontSize = 16.sp
//                                        )
                                        conversation.messageList.sortBy { it.sendDate }
                                        Text(
                                            text = if (conversation.messageList.isEmpty()) "" else conversation.messageList.last().textContent,
                                            color = Color.Gray,
                                            fontSize = 16.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontFamily = CustomFont.font,
                                            modifier = Modifier.weight(0.5f)
                                        )
                                    }
                                }

                                //Time and notification
                                Column(
                                    modifier = Modifier
                                        .weight(0.2f)
                                        .padding(vertical = 15.dp)
                                ) {
                                    Row(modifier = Modifier.weight(0.5f)) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                        ) {
                                            var isSeen = false
                                            if (conversation.messageList.isNotEmpty()){
                                                val tempList= conversation.messageList.reversed()
                                                for (i in 0..<tempList.size){
                                                    Log.e("check"," ")
                                                    if (tempList[i].state.equals("RECEIVED")&&!tempList[i].senderId.equals(Storage.id)){
                                                        isSeen = true
                                                        break
                                                    }
                                                }
                                            }


                                            if (isSeen)
                                                Box(
                                                    modifier = Modifier
                                                        .align(Alignment.Center)
                                                        .clip(RoundedCornerShape(100.dp))
                                                        .size(10.dp)
                                                        .background(color = Color.Red)
                                                )

                                        }
                                    }
                                    Row(
                                        modifier = Modifier.weight(0.5f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (conversation.messageList.isNotEmpty()){
                                            val offsetDateTime = OffsetDateTime.parse(conversation.messageList.last().sendDate)
                                            val messageTime = offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
                                            var messageTimeText = ""
                                            if (messageTime.dayOfYear==today.dayOfYear){
                                                messageTimeText = "${if (messageTime.hour<10) 0 else ""}${messageTime.hour}:${if (messageTime.minute<10) 0 else ""}${messageTime.minute}"
                                            } else {
                                                if ((today.dayOfYear-messageTime.dayOfYear)<messageTime.dayOfWeek.value){
                                                    messageTimeText = messageTime.dayOfWeek.name.substring(0,3)
                                                } else {
                                                    messageTimeText = "${if (messageTime.dayOfMonth<10) 0 else ""}${messageTime.dayOfMonth}-${if (messageTime.monthValue<10) 0 else ""}${messageTime.monthValue}"
                                                }
                                            }
                                            Text(
                                                text = messageTimeText,
                                                color = Color.Gray,
                                                fontSize = 15.sp,
                                                maxLines = 1,
                                                fontWeight = FontWeight.Thin,
                                                textAlign = TextAlign.Center,
                                                overflow = TextOverflow.Ellipsis,
                                                fontFamily = CustomFont.font,
                                                modifier = Modifier.weight(0.5f)
                                            )
                                        }

                                    }
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
                        }
                    }
                }
            }
        }
    }

}