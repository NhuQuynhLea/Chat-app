package com.example.chatapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R

class PropertyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PropertyScene()
        }
    }
}

@Preview
@Composable
fun PropertyScene() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val viewConfiguration = LocalViewConfiguration.current
    val context = LocalContext.current
    var name by remember {
        mutableStateOf("LOL")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height)
                .background(color = colorResource(id = R.color.purple_700))
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


                    }

                }

            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight - viewConfiguration.minimumTouchTargetSize.height * 1.5f)
                .background(Color.LightGray)
        ) {
            item {
                //Avatar function
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Spacer(modifier = Modifier.height(viewConfiguration.minimumTouchTargetSize.height * 0.5f))
                    //Avatar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier.clickable { }) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar),
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .clip(
                                        RoundedCornerShape(100.dp)
                                    )
                                    .size(viewConfiguration.minimumTouchTargetSize.height * 1.5f)
                            )
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "",
                                tint = Color.Gray,
                                modifier = Modifier.align(Alignment.BottomEnd)
                            )
                        }
                    }
                    //Name
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "",
                            tint = Color.Transparent
                        )
                        Spacer(modifier = Modifier.width(viewConfiguration.minimumTouchTargetSize.width * 0.5f))
                        Text(
                            text = name,
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(viewConfiguration.minimumTouchTargetSize.width * 0.5f))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier.clickable { }
                        )
                    }
                    //Function buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(viewConfiguration.minimumTouchTargetSize.height * 1.5f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedIconButton(
                            onClick = { /*TODO*/ },
                            colors = IconButtonDefaults.outlinedIconButtonColors(
                                containerColor = Color.White,
                            ),
                            shape = RoundedCornerShape(15.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            modifier = Modifier.size(
                                height = viewConfiguration.minimumTouchTargetSize.height * 1.5f,
                                width = viewConfiguration.minimumTouchTargetSize.width * 2f
                            )
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "")
                                Text(text = "Tim tin nhan", textAlign = TextAlign.Center)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedIconButton(
                            onClick = { /*TODO*/ },
                            colors = IconButtonDefaults.outlinedIconButtonColors(
                                containerColor = Color.White,
                            ),
                            shape = RoundedCornerShape(15.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            modifier = Modifier.size(
                                height = viewConfiguration.minimumTouchTargetSize.height * 1.5f,
                                width = viewConfiguration.minimumTouchTargetSize.width * 2f
                            )
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.Brush, contentDescription = "")
                                Text(text = "Đổi hình nền", textAlign = TextAlign.Center)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))


                        OutlinedIconButton(
                            onClick = { /*TODO*/ },
                            colors = IconButtonDefaults.outlinedIconButtonColors(
                                containerColor = Color.White,
                            ),
                            shape = RoundedCornerShape(15.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            modifier = Modifier.size(
                                height = viewConfiguration.minimumTouchTargetSize.height * 1.5f,
                                width = viewConfiguration.minimumTouchTargetSize.width * 2f
                            )
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = ""
                                )
                                Text(text = "Tắt thông báo", textAlign = TextAlign.Center)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))
                //Image and link
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "",
                            tint = Color.Black,
                            modifier = Modifier
                                .height(viewConfiguration.minimumTouchTargetSize.height.value.dp * 0.75f)
                                .weight(0.15f)
                        )
                        Column(modifier = Modifier.weight(0.85f)) {
                            Box(
                                modifier = Modifier
                                    .height(viewConfiguration.minimumTouchTargetSize.height.value.dp * 0.65f)
                            ) {
                                Text(
                                    text = "Xem hình ảnh, liên kết",
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.meme),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(viewConfiguration.minimumTouchTargetSize.height * 1.5f)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.meme),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(viewConfiguration.minimumTouchTargetSize.height * 1.5f)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.meme),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(viewConfiguration.minimumTouchTargetSize.height * 1.5f)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .size(viewConfiguration.minimumTouchTargetSize.height * 1.5f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(
                                            width = 1.dp,
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                ) {

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "",
                                        tint = colorResource(id = R.color.mainColor),
                                        modifier = Modifier
                                            .align(
                                                Alignment.Center
                                            )
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        context,
                                                        MultiTaskActivity::class.java
                                                    )
                                                )
                                            }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                //File
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.FileCopy,
                            contentDescription = "",
                            tint = Color.Black,
                            modifier = Modifier
                                .height(viewConfiguration.minimumTouchTargetSize.height.value.dp * 0.75f)
                                .weight(0.15f)
                        )
                        Column(modifier = Modifier.weight(0.85f)) {
                            Box(
                                modifier = Modifier
                                    .height(viewConfiguration.minimumTouchTargetSize.height.value.dp * 0.65f)
                            ) {
                                Text(
                                    text = "Xem file",
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }

                            Column() {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(viewConfiguration.minimumTouchTargetSize.height.value.dp * 1.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.power_point_file),
                                        contentDescription = ""
                                    )
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(text = "Filednfgnfgnnf.ppt", fontSize = 20.sp)
                                        Text(
                                            text = "15.6 MB by Alex",
                                            color = Color.DarkGray,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(viewConfiguration.minimumTouchTargetSize.height.value.dp * 1.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.word_file),
                                        contentDescription = ""
                                    )
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(text = "Filednfgnfgnnf.ppt", fontSize = 20.sp)
                                        Text(
                                            text = "15.6 MB by Alex",
                                            color = Color.DarkGray,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(viewConfiguration.minimumTouchTargetSize.height.value.dp * 1.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.excel_file),
                                        contentDescription = ""
                                    )
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(text = "Filednfgnfgnnf.ppt", fontSize = 20.sp)
                                        Text(
                                            text = "15.6 MB by Alex",
                                            color = Color.DarkGray,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(viewConfiguration.minimumTouchTargetSize.height.value.dp * 0.65f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = { /*TODO*/ },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(5.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.LightGray
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDownward,
                                            contentDescription = "",
                                            tint = colorResource(id = R.color.purple_700)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                //Other function
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(viewConfiguration.minimumTouchTargetSize.height.value.dp)
                            .clickable { },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            tint = Color.Black
                        )
                        Text(text = "Xoá lịch sử trò chuyện", fontSize = 20.sp, color = Color.Black)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(viewConfiguration.minimumTouchTargetSize.height.value.dp)
                            .clickable { },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "",
                            tint = Color.Black
                        )
                        Text(text = "Xoá bạn bè", fontSize = 20.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}