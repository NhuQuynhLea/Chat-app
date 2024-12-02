@file:OptIn(ExperimentalFoundationApi::class)

package com.example.chatapp.activity

import android.app.Activity
import android.content.Intent
import android.icu.text.ListFormatter.Width
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertEmoticon
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.window.Dialog
import com.example.chatapp.R
import com.example.chatapp.model.Message
import com.example.chatapp.network.API
import com.example.chatapp.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import com.example.chatapp.activity.call.CallScreen
import com.example.chatapp.activity.call.CallViewModel
import com.example.chatapp.activity.call.State
import com.example.chatapp.activity.connect.ConnectAction

import com.example.chatapp.activity.connect.ConnectState
import com.example.chatapp.activity.connect.ConnectViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import kotlin.math.ceil

var name = ""
var friendId = ""

class ChatActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = intent.getStringExtra("name") ?: "Guest"
        friendId = intent.getStringExtra("friend_id")?: ""
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ConnectRoute,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<ConnectRoute> {
                        val viewModel = koinViewModel<ConnectViewModel>()
                        val state = viewModel.state


                        LaunchedEffect(key1 = state.isConnected) {
                            if(state.isConnected) {
                                navController.navigate(VideoCallRoute) {
                                    popUpTo(ConnectRoute) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                        viewModel.onAction(ConnectAction.OnNameChange(name))
                        ChatScene(state = state, onAction = viewModel::onAction)
                    }
                    composable<VideoCallRoute> {
                        val viewModel = koinViewModel<CallViewModel>()
                        val state = viewModel.state
                        viewModel.initCall(friendId);

                        LaunchedEffect(key1 = state.callState) {
                            if(state.callState == State.ENDED) {
                                navController.navigate(ConnectRoute) {
                                    popUpTo(VideoCallRoute) {
                                        inclusive = true
                                    }
                                }
                            }
                        }

                        VideoTheme {
                            CallScreen(state = state, onAction = { action ->
                                viewModel.onAction(action, friendId)
                            })
                        }
                    }
                }
            }
        }
    }
}
@kotlinx.serialization.Serializable
data object ConnectRoute

@Serializable
data object VideoCallRoute

@Composable
fun ChatScene(state: ConnectState,
              onAction: (ConnectAction) -> Unit) {
    val chatList = remember {
        mutableStateListOf<String>()
    }
    val chatStateList = remember {
        mutableListOf(true)
    }
    val viewConfiguration = LocalViewConfiguration.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var chatText by remember {
        mutableStateOf("")
    }
    val lazyColumnState = rememberLazyListState()
    val context = LocalContext.current
    val currentDateTime = LocalDateTime.now()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
    ) {
        //MessagesLayout
        LazyColumn(
            state = lazyColumnState,
            reverseLayout = true,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(
                    bottom = viewConfiguration.minimumTouchTargetSize.height + 20.dp,
                    top = viewConfiguration.minimumTouchTargetSize.height * 1.5f
                )
        ) {
            items(Storage.conversationChosen.messageList.reversed()) {
                TextChatItem(
                    content = it.textContent,
                    isSender = it.senderId.equals(Storage.id),
                    screenWidth = screenWidth,
                    screenHeight = screenHeight
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        //TopAppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height)
                .background(color = colorResource(id = R.color.purple_700))
                .align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(0.25f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.clickable {
                                (context as Activity).finish()
                            })

                        Box() {
                            Image(
                                painter = painterResource(id = R.drawable.avatar),
                                contentDescription = "",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .clip(
                                        RoundedCornerShape(100.dp)
                                    )
                            )
                        }

                    }

                }


                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f)
                        .padding(start = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.weight(0.5f), verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = Storage.conversationChosen.name,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                    Row(
                        modifier = Modifier.weight(0.5f), verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "Online",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Column(modifier = Modifier.weight(0.4f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .size(viewConfiguration.minimumTouchTargetSize.height)
                                .padding(5.dp)
                                .clickable {

                                    onAction(ConnectAction.OnConnectClick)
                                }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.call_icon),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color.White),
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .size(viewConfiguration.minimumTouchTargetSize.height)
                                .padding(10.dp)
                                .clickable {

                                    onAction(ConnectAction.OnConnectClick)
                                }
                        )
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .size(viewConfiguration.minimumTouchTargetSize.height)
                                .padding(5.dp)
                                .clickable {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            PropertyActivity::class.java
                                        )
                                    )
                                }
                        )
                    }
                }
            }
        }

        //Input Text
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height + 20.dp)
                .align(Alignment.BottomCenter), elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            ), colors = CardDefaults.cardColors(
                containerColor = Color.White
            ), shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    textStyle = TextStyle(
                        fontSize = 18.sp
                    ),
                    singleLine = false,
                    value = chatText,
                    onValueChange = { chatText = it },
                    shape = RoundedCornerShape(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(id = R.color.messageColor).copy(
                            0.35f
                        ),
                        focusedContainerColor = colorResource(id = R.color.messageColor).copy(0.35f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.InsertEmoticon,
                            contentDescription = "",
                            tint = colorResource(id = R.color.messageColor)
                        )
                    },
                    trailingIcon = {
                        Row(
                            modifier = Modifier.padding(end = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = "",
                                tint = colorResource(id = R.color.messageColor)
                            )
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "",
                                tint = colorResource(id = R.color.messageColor)
                            )
                        }
                    },
                    modifier = Modifier.weight(0.85f)
                )

                Box(modifier = Modifier.weight(0.15f)) {
                    Box(modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(100.dp))
                        .background(colorResource(id = R.color.messageColor))
                        .size(viewConfiguration.minimumTouchTargetSize.height)
                        .clickable {
                            if (chatText
                                    .trim()
                                    .isNotEmpty()
                            ) {
                                val message = Message()
                                message.conversationId = Storage.conversationChosen.id
                                message.textContent = chatText.trim()

                                CoroutineScope(Dispatchers.IO).launch {
                                    API.sendMessage(context = context,message = message, token = Storage.token)
                                    Storage.conversationChosen = API.getOneConversation(context = context, id = Storage.conversationChosen.id, token = Storage.token)
                                    Storage.conversationChosen.messageList.sortBy { it.sendDate }
                                }
                                chatText = ""
                            }
                        }

                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }


            }
        }

    }
}
@Preview(showBackground = true)
@Composable
private fun ChatScene() {
    ChatScene(
        state = ConnectState(
            errorMessage = "Hello world"
        ),
        onAction = {}
    )
}


@Composable
fun TextChatItem(content: String, isSender: Boolean, screenHeight: Dp, screenWidth: Dp) {
    val alignment = if (isSender) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isSender) colorResource(id = R.color.mainColor) else Color.LightGray
    val textColor = if (isSender) Color.White else Color.Black
    val minimumHeight = LocalViewConfiguration.current.minimumTouchTargetSize.height.value.dp
    var showReact by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(15.dp))
                .align(alignment)
                .background(backgroundColor)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        showReact = true
                    }
                )
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            Row(modifier = Modifier.padding(5.dp)) {
                Spacer(modifier = Modifier.width(5.dp))

                Text(text = content, color = textColor, fontSize = 20.sp)

                Spacer(modifier = Modifier.width(5.dp))
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
    if (showReact) Dialog(onDismissRequest = { showReact = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = if (!isSender) Alignment.Start else Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(minimumHeight)
                        .padding(horizontal = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(15.dp))
                            .align(if (!isSender) Alignment.CenterStart else Alignment.CenterEnd)
                            .background(Color.White)
                    ) {
                        Spacer(modifier = Modifier.height(5.dp))

                        Row(modifier = Modifier.padding(5.dp)) {
                            Spacer(modifier = Modifier.width(5.dp))

                            Text(text = content, color = Color.Black, fontSize = 20.sp, textAlign = TextAlign.Center)

                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }

                //Icons
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(20.dp))
//                    .background(Color.White)
//            ) {
//
//            }

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .size(width = screenWidth * 0.5f, height = minimumHeight * 4)
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.25f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Trả lời", color = Color.Black, textAlign = TextAlign.Left)
                        Icon(
                            imageVector = Icons.Default.Reply,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.25f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Chuyển tiếp", color = Color.Black, textAlign = TextAlign.Left)
                        Icon(
                            imageVector = Icons.Default.Reply,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.25f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Sao chép", color = Color.Black, textAlign = TextAlign.Left)
                        Icon(
                            imageVector = Icons.Default.CopyAll,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.25f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Xóa tin nhắn", color = Color.Black, textAlign = TextAlign.Left)
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
}

@Composable
fun SingleImageChatItem(isSender: Boolean) {
    val alignment = if (isSender) Alignment.CenterEnd else Alignment.CenterStart
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.meme),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .size(200.dp)
                .align(alignment)
        )
    }
}

@Composable
fun MultiImageChatItem(isSender: Boolean) {
    val alignment = if (isSender) Alignment.CenterEnd else Alignment.CenterStart
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {

        Box(
            modifier = Modifier
                .size(170.dp)
                .align(alignment)
        ) {
            Image(
                painter = painterResource(id = R.drawable.meme),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .size(150.dp)
                    .blur(10.dp)
                    .align(if (isSender) Alignment.BottomEnd else Alignment.BottomStart)
            )
            Image(
                painter = painterResource(id = R.drawable.meme),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .size(150.dp)
                    .align(if (isSender) Alignment.TopStart else Alignment.TopEnd)
            )
        }

    }
}

@Composable
fun FileChatItem(content: String, size: String, time: String, isSender: Boolean) {
    val alignment = if (isSender) Alignment.CenterEnd else Alignment.CenterStart
    val arrangement = if (isSender) Arrangement.End else Arrangement.Start
    val contentColor = if (isSender) colorResource(id = R.color.messageColor) else Color.LightGray
    val backgroundColor = if (isSender) colorResource(id = R.color.mainColor) else Color.Gray
    val textColor = if (isSender) Color.White else Color.Black
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .align(alignment)
                .width(screenWidth * 0.6f)
                .background(backgroundColor)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .fillMaxWidth()
                        .background(color = contentColor)
                ) {
                    Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)) {
                        Text(text = content, color = textColor, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = size, color = textColor, fontSize = 15.sp)
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(horizontalArrangement = arrangement, modifier = Modifier.fillMaxWidth()) {
                    Text(text = time, color = textColor, fontSize = 15.sp)
                }

            }
        }
    }
}