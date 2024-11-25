package com.example.chatapp.component.mainComponents

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.activity.ChatActivity
import com.example.chatapp.model.User
import com.example.chatapp.network.API
import com.example.chatapp.storage.CustomFont
import com.example.chatapp.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ContactComponent() {
    var searchText by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Storage.listConversation.forEach {
        for (i in 0..it.memberList.size-1){
            var isContain = false
            for (j in 0..Storage.listContact.size-1){
                if (Storage.listContact[j].id.equals(it.memberList[i].id)){
                    isContain = true
                    break
                }
            }
            if (!isContain&&!it.memberList[i].id.equals(Storage.id)){
                Storage.listContact.add(it.memberList[i])
            }
        }
    }
    Storage.listContact.sortBy { it.userName }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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
                    if (searchText.isEmpty()) Text(text = "Tìm kiếm", color = Color.Gray, fontFamily = CustomFont.font)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty())
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                searchText=""
                                focusManager.clearFocus()
                            }
                        )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (Storage.listContact.size!=0)
        LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
            items(count =Storage.listContact.size) { index ->
                if (Storage.listContact[index].userName.lowercase().contains(searchText.trim()))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable {
                            var isExist = false
                            var x=0
                            for (i in 0..Storage.listConversation.size-1){
                                if (Storage.listConversation[i].name.equals(Storage.listContact[index].userName)){
                                    isExist = true
                                    x=i
                                    break
                                }
                            }
                            if (isExist){
                                Storage.conversationChosen=Storage.listConversation[x]
                            } else {
                                API.createConversation(context = context, name = Storage.listContact[index].userName, userIdList = arrayListOf(Storage.listContact[index].id), token = Storage.token)
                                Storage.listConversation = API.getAllConversation(context = context, token = Storage.token)
                                for (i in 0..Storage.listConversation.size-1){
                                    if (Storage.listConversation[i].name.equals(Storage.listContact[index].userName)){
                                        Storage.conversationChosen=Storage.listConversation[i]
                                        break
                                    }
                                }
                            }
                            val intent = Intent(context, ChatActivity::class.java)
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
                            Box(
                                modifier = Modifier
                                    .weight(0.6f)
                                    .fillMaxHeight(),
                            ) {
                                Text(
                                    text = Storage.listContact[index].userName,
                                    color = colorResource(id = R.color.purple_700),
                                    fontSize = 16.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = CustomFont.font,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }

                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
                    }
                }

            }
        }
    }
}