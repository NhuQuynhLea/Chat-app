package com.example.chatapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignInScene()
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}

@Preview
@Composable
fun SignInScene() {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val context = LocalContext.current
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
                        text = "Sign in",
                        color = colorResource(id = R.color.mainColor),
                        fontSize = 30.sp, fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height((screenHeight * 0.05f).dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = accountText, onValueChange = { text -> accountText = text },
                        label = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = "Account", color = Color.Gray)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                    )
                }
                Spacer(modifier = Modifier.height((screenHeight * 0.01f).dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(value = passwordText,
                        onValueChange = { text -> passwordText = text },
                        label = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = "Password", color = Color.Gray)
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
                            Text(text = "Remember", fontSize = 16.sp)
                        }

                    }
                    Column(
                        modifier = Modifier.weight(0.6f),
                        horizontalAlignment = Alignment.End
                    ) {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = "Forgot password ?", fontSize = 16.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height((screenHeight * 0.02f).dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, MainActivity::class.java))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((screenHeight * 0.05f).dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Sign in", color = Color.White, fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.height((screenHeight * 0.02f).dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Don't have account ?", fontSize = 16.sp)
                    TextButton(onClick = {
                        context.startActivity(Intent(context, CreateAccountActivity::class.java))
                    }) {
                        Text(text = "Create account", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}