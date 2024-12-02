@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.R
import com.example.chatapp.component.mainComponents.ContactComponent
import com.example.chatapp.component.mainComponents.MessagesComponent
import com.example.chatapp.component.mainComponents.ProfileComponent
import com.example.chatapp.define.Define
import com.example.chatapp.model.ViewModelTest
import com.example.chatapp.network.API
import com.example.chatapp.network.SSEClient
import com.example.chatapp.network.SSEHandler
import com.example.chatapp.network.SSEService
import com.example.chatapp.storage.CustomFont
import com.example.chatapp.storage.Storage
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.chatapp.activity.call.CallScreen
import com.example.chatapp.activity.call.CallViewModel
import com.example.chatapp.activity.call.State


import com.example.chatapp.activity.connect.ConnectViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.time.LocalTime
import java.util.LinkedList
import java.util.Queue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopService(Intent(this, SSEService::class.java))
        startService(Intent(this, SSEService::class.java))
        setContent {
            MainScene()
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
            val mPrefEditor = sharedPreferences.edit()
            mPrefEditor.putBoolean(Define.LOGIN_STATE,false)
            mPrefEditor.apply()
            finishAffinity()
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}

@Preview
@Composable
fun MainScene() {
    var sceneState by remember {
        mutableStateOf("Tin nhắn")
    }
    val context = LocalContext.current
    val viewConfiguration = LocalViewConfiguration.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var addMenuExpanded by remember {
        mutableStateOf(false)
    }
    var isLoading by remember {
        mutableStateOf(true)
    }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            Storage.listConversation = API.getAllConversation(context = context, Storage.token)
            isLoading = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) {
        Log.e("storage: ",Storage.id )
        //Heading
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height)
                .background(color = colorResource(id = R.color.purple_700))
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(viewConfiguration.minimumTouchTargetSize.height)
                    .background(
                        color = colorResource(id = R.color.purple_700)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sceneState,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold, fontFamily = CustomFont.font
                )
                Box() {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .clickable {
                                addMenuExpanded = !addMenuExpanded
                            }
                            .align(Alignment.Center)
                    )
                    DropdownMenu(
                        expanded = addMenuExpanded,
                        onDismissRequest = { addMenuExpanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .align(Alignment.Center)
                    ) {
                        DropdownMenuItem(text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAddAlt1,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                                Text(text = "Thêm bạn", textAlign = TextAlign.Center)
                            }
                        }, onClick = {
                            val intent = Intent(context,MultiTaskActivity::class.java)
                            intent.putExtra("type","friend")
                            context.startActivity(intent)
                        })
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
                        DropdownMenuItem(text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GroupAdd,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                                Text(
                                    text = "Tạo nhóm",
                                    textAlign = TextAlign.Center,
                                    fontFamily = CustomFont.font
                                )
                            }
                        }, onClick = {
                            val intent = Intent(context,MultiTaskActivity::class.java)
                            intent.putExtra("type","group")
                            context.startActivity(intent)
                        })
                    }

                }
            }

        }

        //MainContainer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .height(screenHeight - viewConfiguration.minimumTouchTargetSize.height * 2.2f)
        ) {
            if (!isLoading) {
                when (sceneState) {
                    "Tin nhắn" -> MessagesComponent()
                    "Liên hệ" -> ContactComponent()
                    "Tài khoản" -> ProfileComponent()
                }
            }

        }

        //BottomBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height * 1.2f)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .background(Color.White)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.messenger_icon),
                contentDescription = "Tin nhắn",
                modifier = Modifier
                    .weight(1 / 3f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            sceneState = "Tin nhắn"
                        }
                    }
                    .padding(10.dp),
                colorFilter = if (sceneState.equals("Tin nhắn")) {
                    ColorFilter.tint(colorResource(id = R.color.purple_700))
                } else {
                    ColorFilter.tint(Color.LightGray)
                }
            )

            Image(
                painter = painterResource(id = R.drawable.call_icon),
                contentDescription = "Liên hệ",
                modifier = Modifier
                    .weight(1 / 3f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            sceneState = "Liên hệ"
                        }
                    }
                    .padding(10.dp),
                colorFilter = if (sceneState.equals("Liên hệ")) {
                    ColorFilter.tint(colorResource(id = R.color.purple_700))
                } else {
                    ColorFilter.tint(Color.LightGray)
                }
            )

            Image(
                painter = painterResource(id = R.drawable.person_icon),
                contentDescription = "Tài khoản",
                modifier = Modifier
                    .weight(1 / 3f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            sceneState = "Tài khoản"
                        }
                    }
                    .padding(10.dp),
                colorFilter = if (sceneState.equals("Tài khoản")) {
                    ColorFilter.tint(colorResource(id = R.color.purple_700))
                } else {
                    ColorFilter.tint(Color.LightGray)
                }
            )
        }
    }
    if (isLoading) LoadingDialog()
}