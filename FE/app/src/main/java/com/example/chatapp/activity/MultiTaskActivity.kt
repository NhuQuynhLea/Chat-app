package com.example.chatapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.model.User
import com.example.chatapp.network.API
import com.example.chatapp.storage.CustomFont
import com.example.chatapp.storage.Storage
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class MultiTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            intent.let { MultiTaskScene(it) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MultiTaskScene(intent: Intent) {
    val viewConfiguration = LocalViewConfiguration.current
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var searchText by remember {
        mutableStateOf("")
    }
    var groupName by remember {
        mutableStateOf("")
    }
    var searchUser = remember {
        mutableStateListOf<User>()
    }
    var chosenUser = remember {
        mutableStateListOf<User>()
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
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

            if (intent.getStringExtra("type").equals("group")) Row(
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
                            Text(
                                text = "Tên nhóm",
                                fontSize = 20.sp,
                                fontFamily = CustomFont.font,
                                fontWeight = FontWeight.Medium
                            )
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
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .background(Color.Transparent)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    shape = RoundedCornerShape(10.dp),
                    placeholder = {
                        if (searchText.isEmpty()) Text(
                            text = "Tìm kiếm", color = Color.Gray, fontFamily = CustomFont.font
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "",
                            tint = Color.Gray
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) Icon(imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                searchText = ""
                                focusManager.clearFocus()
                            })
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                        if (searchText.trim().isNotEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                isLoading = true
                                searchUser.clear()
                                API.searchUser(context = context, query = searchText.trim(), token = Storage.token).forEach {
                                    var isContain = false
                                    if (chosenUser.isNotEmpty()){
                                        for (i in 0..<chosenUser.size){
                                            if (it.id.equals(chosenUser[i].id)){
                                                isContain = true
                                                break
                                            }
                                        }
                                    }
                                    if (!isContain) searchUser.add(it)
                                }
                                isLoading= false
                            }
                        }
                    }),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            LazyColumn() {
                items(chosenUser){ item->
                    var checkBoxItem by remember {
                        mutableStateOf(true)
                    }
                    if (!checkBoxItem) {
                        chosenUser.remove(item)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
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
                                Box(
                                    modifier = Modifier
                                        .weight(0.6f)
                                        .fillMaxHeight(),
                                ) {
                                    Text(
                                        text = item.userName,
                                        color = colorResource(id = R.color.purple_700),
                                        fontSize = 16.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = CustomFont.font,
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                }
                                //Checkbox
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
                items(searchUser){ item->
                    var checkBoxItem by remember {
                        mutableStateOf(false)
                    }
                    if (checkBoxItem) {
                        chosenUser.add(item)
                        searchUser.remove(item)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
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
                                Box(
                                    modifier = Modifier
                                        .weight(0.6f)
                                        .fillMaxHeight(),
                                ) {
                                    Text(
                                        text = item.userName,
                                        color = colorResource(id = R.color.purple_700),
                                        fontSize = 16.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = CustomFont.font,
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                }
                                //Checkbox
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
                .padding(horizontal = 10.dp), horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = {
                    (context as Activity).finish()
                },
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
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        isLoading = true
                        if (intent.getStringExtra("type").equals("friend")) {
                            for (index in 0..chosenUser.size - 1) {
                                var isExist = false
                                for (i in 0..Storage.listConversation.size - 1) {
                                    if (Storage.listConversation[i].name.equals(chosenUser[index])) {
                                        isExist = true
                                        break
                                    }
                                }
                                if (!isExist) {
                                    var conversation = API.createConversation(
                                        context = context,
                                        name = chosenUser[index].userName+"/"+Storage.userName,
                                        userIdList = arrayListOf(chosenUser[index].id),
                                        token = Storage.token
                                    )
                                    Storage.listConversation = API.getAllConversation(
                                        context = context, token = Storage.token
                                    )
                                    Storage.conversationChosen = conversation;
                                }
                            }
                            context.startActivity(Intent(context,MainActivity::class.java))
                            (context as Activity).finish()
                        } else {
                            if (groupName.isNotEmpty()){
                                var isExist = false
                                for (i in 0..Storage.listConversation.size - 1) {
                                    if (Storage.listConversation[i].name.equals(groupName)) {
                                        isExist = true
                                        break
                                    }
                                }
                                if (!isExist) {
                                    var chosenUserId = arrayListOf<String>()
                                    chosenUser.forEach { chosenUserId.add(it.id) }

                                    var conversation = API.createConversation(
                                        context = context,
                                        name = groupName,
                                        userIdList = chosenUserId,
                                        token = Storage.token
                                    )
                                    Storage.listConversation =
                                        API.getAllConversation(context = context, token = Storage.token)
                                }
                                context.startActivity(Intent(context,MainActivity::class.java))
                                (context as Activity).finish()
                            } else {
                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(context,"Please enter group name!",Toast.LENGTH_LONG).show()
                                }
                            }


                        }
                        isLoading=false
                    }

                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = colorResource(id = R.color.blue))
            ) {
                Text(
                    text = if (intent.getStringExtra("type").equals("group")) "Tạo" else "Thêm",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = CustomFont.font
                )
            }
        }
    }
    if (isLoading) LoadingDialog()
}