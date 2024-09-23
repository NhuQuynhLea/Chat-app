package com.example.chatapp.activity.component

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.activity.ChatActivity
import java.time.LocalTime

@SuppressLint("NotConstructor")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesComponent() {
    val name = listOf("Dương quá", "Nguyễn Cao Hà Phương", "Quỳnh Lea", "Trần Trung Kiên")
    val time = LocalTime.now()
    var searchText by remember {
        mutableStateOf("")
    }
    var searchActive by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(Color.Transparent)
        ) {
            SearchBar(
                query = searchText,
                onQueryChange = {},
                onSearch = { searchActive = false },
                active = searchActive,
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White
                ),
                shadowElevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                onActiveChange = { searchActive = it },
                placeholder = {
                    Text(text = "Search", color = Color.Gray)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchActive)
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                if (searchText.isNotEmpty()) {
                                    searchText = ""
                                } else {
                                    searchActive = false
                                }
                            }
                        )
                }) {
            }
        }

        LazyColumn() {
            items(count = 20) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable {
                            val intent = Intent(context, ChatActivity::class.java)
                            intent.putExtra("name",name[item%4])
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
                                        text = name[item % 4],
                                        color = colorResource(id = R.color.purple_700),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                Row(
                                    modifier = Modifier.weight(0.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (name[item % 4].equals("Trần Trung Kiên")) {
                                        Text(
                                            text = "Bản nháp: ",
                                            color = colorResource(id = R.color.mainColor),
                                            fontSize = 16.sp
                                        )
                                    }

                                    Text(
                                        text = "qwertyuiopasdfghjklzxcvbnmqwertyuiosdfghjklxcvbn",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
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
                                        text = time.hour.toString() + ":" + time.minute.toString(),
                                        color = Color.Gray,
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        fontWeight = FontWeight.Thin,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
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