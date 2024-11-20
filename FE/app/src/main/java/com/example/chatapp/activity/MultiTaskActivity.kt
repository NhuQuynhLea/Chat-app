package com.example.chatapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.storage.CustomFont

class MultiTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultiTaskScene()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MultiTaskScene() {
    val viewConfiguration = LocalViewConfiguration.current
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val name = listOf("Dương quá", "Nguyễn Cao Hà Phương", "Quỳnh Lea", "Trần Trung Kiên")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        //TopAppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height)
                .background(color = colorResource(id = R.color.purple_700))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .clickable {
                        (context as Activity).finish()
                    })
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight - viewConfiguration.minimumTouchTargetSize.height.value.dp * 2f)
        ) {

            var searchText by remember {
                mutableStateOf("")
            }
            var searchActive by remember {
                mutableStateOf(false)
            }
            var groupName by remember {
                mutableStateOf("")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(viewConfiguration.minimumTouchTargetSize.height * 2f)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraEnhance,
                    contentDescription = "",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(viewConfiguration.minimumTouchTargetSize.height * 2f)
                        .padding(10.dp)
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = groupName,
                        onValueChange = { groupName = it },
                        label = {
                            Text(text = "Tên nhóm", fontSize = 20.sp, fontFamily = CustomFont.font, fontWeight = FontWeight.Medium)
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Blue,
                            focusedIndicatorColor = Color.Blue
                        ),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(Color.Transparent)
            ) {
                SearchBar(
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = { searchActive = false },
                    active = searchActive,
                    colors = SearchBarDefaults.colors(
                        containerColor = Color.White
                    ),
                    shadowElevation = 20.dp,
                    shape = RoundedCornerShape(10.dp),
                    onActiveChange = { searchActive = it },
                    placeholder = {
                        Text(text = "Tìm kiếm", color = Color.Gray, fontFamily = CustomFont.font)
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
                    var checkBoxItem by remember {
                        mutableStateOf(false)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clickable {
                                val intent = Intent(context, ChatActivity::class.java)
                                intent.putExtra("name", name[item % 4])
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
                                        .padding(vertical = 20.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.weight(0.5f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = name[item % 4],
                                            color = colorResource(id = R.color.purple_700),
                                            fontSize = 20.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = CustomFont.font
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
                                            text = "1234567890",
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
                                Box(
                                    modifier = Modifier
                                        .weight(0.2f)
                                        .fillMaxHeight()
                                ) {
                                    Checkbox(
                                        checked = checkBoxItem, onCheckedChange = {
                                            checkBoxItem = !checkBoxItem
                                        }, colors = CheckboxDefaults.colors(
                                            checkmarkColor = Color.White,
                                            checkedColor = Color.Green
                                        ),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
                        }
                    }
                }
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height.value.dp)
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Hủy",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontFamily = CustomFont.font
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = colorResource(id = R.color.blue))
            ) {
                Text(
                    text = "Thêm",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = CustomFont.font
                )
            }
        }
    }
}