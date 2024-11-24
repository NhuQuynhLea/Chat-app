package com.example.chatapp.component.mainComponents

import android.content.Intent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.chatapp.storage.CustomFont
import com.example.chatapp.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MessagesComponent() {
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
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
            items(Storage.listConversation) { conversation ->
                if (conversation.name.lowercase().contains(searchText.trim()))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable {
                            val intent = Intent(context, ChatActivity::class.java)
                            Storage.conversationChosen = conversation
                            context.startActivity(intent)
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
                                    if (conversation.memberList.size == 1) {
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
                                        text = conversation.name,
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

                                    Text(
                                        text = "qwertyuiopasdfghjklzxcvbnmqwertyuiosdfghjklxcvbn",
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
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .clip(RoundedCornerShape(100.dp))
                                                .size(20.dp)
                                                .background(color = Color.Red)
                                        ) {
                                            Text(
                                                text = "2",
                                                color = Color.White,
                                                fontFamily = CustomFont.font,
                                                modifier = Modifier.align(
                                                    Alignment.Center
                                                )
                                            )
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.weight(0.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "",
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
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
                    }
                }
            }
        }
    }

}