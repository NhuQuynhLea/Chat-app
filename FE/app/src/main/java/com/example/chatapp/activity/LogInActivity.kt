package com.example.chatapp.activity

import android.app.Activity.MODE_PRIVATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.chatapp.R
import com.example.chatapp.define.Define
import com.example.chatapp.network.API
import com.example.chatapp.storage.CustomFont
import com.example.chatapp.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class LogInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notificationChannel = NotificationChannel(
            "notification_channel_id",
            "Notification name",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        setContent {
            LogInScene()
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Bấm QUAY LẠI lần nữa để thoát", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}

@Preview
@Composable
fun LogInScene() {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(context.packageName, MODE_PRIVATE)
    val mPrefEditor = sharedPreferences.edit()

    var checkBoxRemember by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    var accountText by remember {
        mutableStateOf("")
    }
    var passwordText by remember {
        mutableStateOf("")
    }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }


    Image(
        painter = painterResource(id = R.drawable.main_background),
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(height = (screenHeight / 2).dp, width = (screenWidth / 1.2).dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Đăng nhập",
                        color = colorResource(id = R.color.mainColor),
                        fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = CustomFont.font
                    )
                }
                Spacer(modifier = Modifier.height((screenHeight * 0.05f).dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = accountText, onValueChange = { text -> accountText = text },
                        label = {
                            Text(text = "Email", color = Color.Gray, fontFamily = CustomFont.font)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                    )
                }
                Spacer(modifier = Modifier.height((screenHeight * 0.01f).dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = passwordText,
                        onValueChange = { text -> passwordText = text },
                        label = {
                            Text(text = "Mật khẩu", color = Color.Gray, fontFamily = CustomFont.font)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "",
                                tint = Color.Gray
                            )
                        },
                        visualTransformation = if (isPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(0.4f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkBoxRemember, onCheckedChange = {
                                    checkBoxRemember = !checkBoxRemember
                                }, colors = CheckboxDefaults.colors(
                                    checkmarkColor = Color.White,
                                    checkedColor = Color.Green
                                )
                            )
                            Text(text = "Nhớ tôi", fontSize = 16.sp, fontFamily = CustomFont.font)
                        }

                    }
                    Column(
                        modifier = Modifier.weight(0.6f),
                        horizontalAlignment = Alignment.End
                    ) {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = "Quên mật khẩu?", fontSize = 16.sp, fontFamily = CustomFont.font)
                        }
                    }
                }
                Spacer(modifier = Modifier.height((screenHeight * 0.02f).dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {

                            CoroutineScope(Dispatchers.IO).launch {
                                isLoading = true
                                val token = API.getAuthenticatedToken(context = context, username = accountText.trim(),password = passwordText.trim())
                                Log.e("check",token)
                                if (token.isNotEmpty()){
                                    val user = API.getAuthenticatedUser(context = context, token = token)
                                    if (user.id.isNotEmpty()){
                                        Storage.id = user.id
                                        Storage.password = user.password
                                        Storage.userName = user.userName
                                        Storage.token = token
                                        Storage.email = user.email
                                        Storage.imageUrl = user.imageUrl
                                        if (checkBoxRemember){
                                            mPrefEditor.putBoolean(Define.REMEMBER,true)
                                            mPrefEditor.putString(Define.USER_NAME,accountText.trim())
                                            mPrefEditor.putString(Define.USER_PASSWORD,passwordText.trim())
                                        }
                                        mPrefEditor.putBoolean(Define.LOGIN_STATE,true)
                                        mPrefEditor.apply()
                                        context.startActivity(Intent(context,MainActivity::class.java))
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "Đăng nhập không thành công", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Đăng nhập không thành công", Toast.LENGTH_LONG).show()
                                    }
                                }
                                isLoading=false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((screenHeight * 0.05f).dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Đăng nhập", color = Color.White, fontSize = 18.sp, fontFamily = CustomFont.font)
                    }
                }
                Spacer(modifier = Modifier.height((screenHeight * 0.02f).dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Không có tài khoản ?", fontSize = 16.sp, fontFamily = CustomFont.font)
                    TextButton(onClick = {
                        context.startActivity(Intent(context, CreateAccountActivity::class.java))
                    }) {
                        Text(text = "Tạo tài khoản", fontSize = 16.sp, fontFamily = CustomFont.font)
                    }
                }
            }
        }
    }
    if (isLoading) LoadingDialog()
}

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"

    // Key for the string that's delivered in the action's intent.

    val snoozeIntent = Intent(context, BroadcastReceiver::class.java)
    val snoozePendingIntent: PendingIntent =
        PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE)

    // SIMPLE NOTIFICATION
    fun showSimpleNotification() {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setSmallIcon(R.drawable.google_logo)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .addAction(R.drawable.call_icon,"Yolo",
                snoozePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}