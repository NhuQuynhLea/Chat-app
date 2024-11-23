@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.R
import com.example.chatapp.activity.call.CallScreen
import com.example.chatapp.activity.call.CallViewModel
import com.example.chatapp.activity.call.State
import com.example.chatapp.activity.component.ContactComponent
import com.example.chatapp.activity.component.MessagesComponent
import com.example.chatapp.activity.component.PrivateComponent
import com.example.chatapp.activity.connect.ConnectScreen
import com.example.chatapp.activity.connect.ConnectViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.time.LocalTime
import java.util.LinkedList
import java.util.Queue

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MainScene()
//        }
//    }
//    private var doubleBackToExitPressedOnce = false
//    override fun onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed()
//            return
//        }
//
//        this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
//
//        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
//    }
//}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScene()
//            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                val navController = rememberNavController()
//                NavHost(
//                    navController = navController,
//                    startDestination = ConnectRoute,
//                    modifier = Modifier.padding(innerPadding)
//                ) {
//                    composable<ConnectRoute> {
//                        val viewModel = koinViewModel<ConnectViewModel>()
//                        val state = viewModel.state
//
//                        LaunchedEffect(key1 = state.isConnected) {
//                            if(state.isConnected) {
//                                navController.navigate(VideoCallRoute) {
//                                    popUpTo(ConnectRoute) {
//                                        inclusive = true
//                                    }
//                                }
//                            }
//                        }
//
//                        ConnectScreen(state = state, onAction = viewModel::onAction)
//                    }
//                    composable<VideoCallRoute> {
//                        val viewModel = koinViewModel<CallViewModel>()
//                        val state = viewModel.state
//
//                        LaunchedEffect(key1 = state.callState) {
//                            if(state.callState == State.ENDED) {
//                                navController.navigate(ConnectRoute) {
//                                    popUpTo(VideoCallRoute) {
//                                        inclusive = true
//                                    }
//                                }
//                            }
//                        }
//
//                        VideoTheme {
//
//                            CallScreen(state = state, onAction = viewModel::onAction)
//                        }
//                    }
//                }
//            }
        }
    }
}

//@kotlinx.serialization.Serializable
//data object ConnectRoute
//
//@Serializable
//data object VideoCallRoute
@Preview
@Composable
fun MainScene() {
    var sceneState by remember {
        mutableStateOf("Contact")
    }
    val viewConfiguration = LocalViewConfiguration.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var addMenuExpanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        //Heading
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height * 1.5f)
                .background(color = colorResource(id = R.color.purple_700))
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(viewConfiguration.minimumTouchTargetSize.height * 1.5f)
                    .background(
                        color = colorResource(id = R.color.purple_700)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sceneState,
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Box() {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.clickable {
                            addMenuExpanded = !addMenuExpanded
                        }.align(Alignment.Center)
                    )
                    DropdownMenu(
                        expanded = addMenuExpanded,
                        onDismissRequest = { addMenuExpanded = false },
                        modifier = Modifier.background(Color.White).align(Alignment.Center)
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
                        }, onClick = { /*TODO*/ })
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
                                Text(text = "Tạo nhóm")
                            }
                        }, onClick = { /*TODO*/ })
                    }

                }
            }

        }

        //MainContainer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .height(screenHeight - viewConfiguration.minimumTouchTargetSize.height * 2.7f)
        ) {
            when (sceneState) {
                "Messages" -> MessagesComponent()
                "Contact" -> ContactComponent()
                "Private" -> PrivateComponent()
            }

        }

        //BottomBar
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ), // Adjust the elevation as needed
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(viewConfiguration.minimumTouchTargetSize.height * 1.2f)
        ) {
            OutlinedCard(
                border = BorderStroke(
                    1.dp,
                    colorResource(id = R.color.mainColor).copy(alpha = 0.3f)
                ), // Adjust the border width and color
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.messenger_icon),
                        contentDescription = "Messages",
                        modifier = Modifier
                            .weight(1 / 3f)
                            .fillMaxHeight()
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    sceneState = "Messages"
                                }
                            }
                            .padding(10.dp),
                        colorFilter = if (sceneState.equals("Messages")) {
                            ColorFilter.tint(colorResource(id = R.color.purple_700))
                        } else {
                            ColorFilter.tint(Color.LightGray)
                        }
                    )

                    Image(
                        painter = painterResource(id = R.drawable.call_icon),
                        contentDescription = "Contact",
                        modifier = Modifier
                            .weight(1 / 3f)
                            .fillMaxHeight()
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    sceneState = "Contact"
                                }
                            }
                            .padding(10.dp),
                        colorFilter = if (sceneState.equals("Contact")) {
                            ColorFilter.tint(colorResource(id = R.color.purple_700))
                        } else {
                            ColorFilter.tint(Color.LightGray)
                        }
                    )

                    Image(
                        painter = painterResource(id = R.drawable.person_icon),
                        contentDescription = "Private",
                        modifier = Modifier
                            .weight(1 / 3f)
                            .fillMaxHeight()
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    sceneState = "Private"
                                }
                            }
                            .padding(10.dp),
                        colorFilter = if (sceneState.equals("Private")) {
                            ColorFilter.tint(colorResource(id = R.color.purple_700))
                        } else {
                            ColorFilter.tint(Color.LightGray)
                        }
                    )
                }

            }
        }

    }

}