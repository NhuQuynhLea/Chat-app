package com.example.chatapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.chatapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Image(painter = painterResource(id = R.drawable.splash_background), contentDescription = "", modifier = Modifier.fillMaxSize())

    LaunchedEffect(Unit) {
        scope.launch {
            delay(1000)
            val intent = Intent(context, SignInActivity::class.java)
            context.startActivity(intent)
        }
    }
}