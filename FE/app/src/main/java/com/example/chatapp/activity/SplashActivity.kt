package com.example.chatapp.activity

import android.app.Activity.MODE_PRIVATE
import android.app.Activity.NOTIFICATION_SERVICE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.NotificationCompat
import com.example.chatapp.R
import com.example.chatapp.define.Define
import com.example.chatapp.network.API
import com.example.chatapp.network.SSEService
import com.example.chatapp.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class SplashActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()
        }
    }
}


@Composable
fun SplashScreen(){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(context.packageName, MODE_PRIVATE)

    Image(painter = painterResource(id = R.drawable.splash_background), contentDescription = "", modifier = Modifier.fillMaxSize())
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            if (sharedPreferences.getBoolean(Define.LOGIN_STATE,false)){
                val token = API.getAuthenticatedToken(context = context, username = sharedPreferences.getString(Define.USER_NAME,"")!!,password = sharedPreferences.getString(Define.USER_PASSWORD,"")!!)
                if (token.isNotEmpty()){
                    val user = API.getAuthenticatedUser(context = context, token = token)
                    if (user.id.isNotEmpty()){
                        Storage.id = user.id
                        Storage.password = user.password
                        Storage.userName = user.userName
                        Storage.token = token
                        Storage.email = user.email
                        Storage.imageUrl = user.imageUrl

                        context.startActivity(Intent(context,MainActivity::class.java))
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context,"Đăng nhập không thành công",Toast.LENGTH_LONG).show()
                        }
                        context.startActivity(Intent(context,LogInActivity::class.java))
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context,"Đăng nhập không thành công",Toast.LENGTH_LONG).show()
                    }
                    context.startActivity(Intent(context,LogInActivity::class.java))
                }
            } else {
                if (sharedPreferences.getBoolean(Define.REMEMBER,false)){
                    val token = API.getAuthenticatedToken(context = context, username = sharedPreferences.getString(Define.USER_NAME,"")!!,password = sharedPreferences.getString(Define.USER_PASSWORD,"")!!)
                    if (token.isNotEmpty()){
                        val user = API.getAuthenticatedUser(context = context, token = token)
                        if (user.id.isNotEmpty()){
                            Storage.id = user.id
                            Storage.password = user.password
                            Storage.userName = user.userName
                            Storage.token = token
                            Storage.email = user.email
                            Storage.imageUrl = user.imageUrl

                            context.startActivity(Intent(context,MainActivity::class.java))
                        } else {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(context,"Đăng nhập không thành công",Toast.LENGTH_LONG).show()
                            }
                            context.startActivity(Intent(context,LogInActivity::class.java))
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context,"Đăng nhập không thành công",Toast.LENGTH_LONG).show()
                        }
                        context.startActivity(Intent(context,LogInActivity::class.java))
                    }
                } else {
                    delay(1000)
                    context.startActivity(Intent(context,LogInActivity::class.java))
                }
            }
        }
    }
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .size(175.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Transparent),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center), color = Color.White
            )
        }
    }
}

