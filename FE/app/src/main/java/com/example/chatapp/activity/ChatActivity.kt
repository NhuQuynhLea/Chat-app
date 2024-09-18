package com.example.chatapp.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertEmoticon
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.motionEventSpy
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.chatapp.R
import kotlin.math.ceil

var name = ""

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = intent.getStringExtra("name").toString()
        setContent {
            ChatScene()
        }
    }
}

@Preview
@Composable
fun ChatScene() {
    val chatList = remember {
        mutableStateListOf<String>()
    }
    val chatStateList = remember {
        mutableListOf(true)
    }
    val viewConfiguration = LocalViewConfiguration.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var chatText by remember {
        mutableStateOf("")
    }
    val lazyColumnState = rememberLazyListState()
    val context = LocalContext.current
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
                .padding(bottom = viewConfiguration.minimumTouchTargetSize.height + 20.dp, top = viewConfiguration.minimumTouchTargetSize.height*1.5f)
        ) {
            item {
                TextChatItem(content = "Hello", isSender = true)
                Spacer(modifier = Modifier.height(10.dp))
                TextChatItem(content = "Hello", isSender = false)
                Spacer(modifier = Modifier.height(10.dp))
                SingleImageChatItem(false)
                Spacer(modifier = Modifier.height(10.dp))
                MultiImageChatItem(isSender = true)
                Spacer(modifier = Modifier.height(10.dp))
                FileChatItem(content = "test.png", size = "20 MB", time = "10:00", isSender = true)
                Spacer(modifier = Modifier.height(10.dp))
                FileChatItem(content = "test.jpeg", size = "20 MB", time = "10:00", isSender = false)
                Spacer(modifier = Modifier.height(10.dp))
                FileChatItem(content = "test.jpeg", size = "20 MB", time = "10:00", isSender = false)
            }
        }

        //TopAppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height * 1.5f)
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
                                contentScale = ContentScale.FillBounds,
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
                            text = name,
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
                        )
                        Image(
                            painter = painterResource(id = R.drawable.call_icon),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color.White),
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .size(viewConfiguration.minimumTouchTargetSize.height)
                                .padding(10.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .size(viewConfiguration.minimumTouchTargetSize.height)
                                .padding(5.dp)
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
                                chatList.add(chatText.trim())
                                chatStateList.add(true)
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

@Composable
fun TextChatItem(content: String, isSender: Boolean) {
    val alignment = if (isSender) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isSender) colorResource(id = R.color.mainColor) else Color.LightGray
    val textColor = if (isSender) Color.White else Color.Black
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
fun FileChatItem(content: String, size:String, time:String, isSender: Boolean) {
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