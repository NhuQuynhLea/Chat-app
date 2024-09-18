package com.example.chatapp.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R

class CreateAccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateAccountScene()
        }
    }
}

@Preview
@Composable
fun CreateAccountScene() {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val focusManager = LocalFocusManager.current
    var emailText by remember {
        mutableStateOf("")
    }
    var passwordText by remember {
        mutableStateOf("")
    }
    var nameText by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
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
                .size(height = (screenHeight / 1.8).dp, width = (screenWidth / 1.2).dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedIconButton(onClick = { (context as Activity).finish() },
                        border = BorderStroke(color = colorResource(id = R.color.mainColor), width = 1.dp),
                        modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(0.75f),
                            tint = colorResource(
                                id = R.color.mainColor
                            )
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Create an account",
                        color = colorResource(id = R.color.mainColor),
                        fontSize = 30.sp, fontWeight = FontWeight.Bold
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = { text -> nameText = text },
                        label = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = "Full name", color = Color.Gray)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = emailText,
                        onValueChange = { text -> emailText = text },
                        label = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = "Email", color = Color.Gray)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = passwordText,
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((screenHeight * 0.05f).dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.mainColor)
                        )
                    ) {
                        Text(text = "Sign up", color = Color.White, fontSize = 18.sp)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black)
                            .height(1.dp)
                            .weight(0.15f)
                    )
                    Text(
                        text = "Or sign up with",
                        fontSize = 20.sp,
                        modifier = Modifier.weight(0.3f),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.Black)
                            .height(1.dp)
                            .weight(0.15f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    ElevatedButton(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Google",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                    }
                }
            }
        }
    }
}
