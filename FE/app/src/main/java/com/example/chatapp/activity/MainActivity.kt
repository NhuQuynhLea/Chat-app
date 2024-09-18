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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.chatapp.R
import java.time.LocalTime
import java.util.LinkedList
import java.util.Queue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScene()
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

@Composable
fun MessagesComponent() {
    val name = listOf("Dương quá", "Nguyễn Cao Hà Phương", "Quỳnh Lea", "Trần Trung Kiên")
    val time = LocalTime.now()
    var searchText by remember {
        mutableStateOf("")
    }
    var searchActive by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(Color.Transparent)
        ) {
            SearchBar(
                query = searchText,
                onQueryChange = {},
                onSearch = { searchActive = false },
                active = searchActive,
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White
                ),
                shadowElevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                onActiveChange = { searchActive = it },
                placeholder = {
                    Text(text = "Search", color = Color.Gray)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchActive)
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                if (searchText.isNotEmpty()) {
                                    searchText = ""
                                } else {
                                    searchActive = false
                                }
                            }
                        )
                }) {
            }
        }

        LazyColumn() {
            items(count = 20) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable {
                            val intent = Intent(context,ChatActivity::class.java)
                            intent.putExtra("name",name[item%4])
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
                            Column(
                                modifier = Modifier
                                    .weight(0.6f)
                                    .padding(vertical = 20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.weight(0.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = name[item % 4],
                                        color = colorResource(id = R.color.purple_700),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                Row(
                                    modifier = Modifier.weight(0.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (name[item % 4].equals("Trần Trung Kiên")) {
                                        Text(
                                            text = "Bản nháp: ",
                                            color = colorResource(id = R.color.mainColor),
                                            fontSize = 16.sp
                                        )
                                    }

                                    Text(
                                        text = "qwertyuiopasdfghjklzxcvbnmqwertyuiosdfghjklxcvbn",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(0.5f)
                                    )
                                }
                            }

                            //Time and notification
                            Column(
                                modifier = Modifier
                                    .weight(0.2f)
                                    .padding(vertical = 15.dp)
                            ) {
                                Row(modifier = Modifier.weight(0.5f)) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .clip(RoundedCornerShape(100.dp))
                                                .size(20.dp)
                                                .background(color = Color.Red)
                                        ) {
                                            Text(
                                                text = "2",
                                                color = Color.White,
                                                modifier = Modifier.align(
                                                    Alignment.Center
                                                )
                                            )
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.weight(0.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = time.hour.toString() + ":" + time.minute.toString(),
                                        color = Color.Gray,
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        fontWeight = FontWeight.Thin,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(0.5f)
                                    )
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
                    }
                }
            }
        }
    }

}

@Composable
fun ContactComponent() {
    var name = listOf("Trần Trung Kiên","Quỳnh Lea", "Dương quá", "Quỳnh Lea", "Nguyễn Cao Hà Phương")
    name = name.sorted()
    val type: Queue<String> = LinkedList()
    name.forEach {
        if (!type.contains(it[0].toString().uppercase())) type.add(it[0].toString().uppercase())
    }
    var currentType = ""
    var searchText by remember {
        mutableStateOf("")
    }
    var searchActive by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(Color.Transparent)
        ) {
            SearchBar(
                query = searchText,
                onQueryChange = {},
                onSearch = { searchActive = false },
                active = searchActive,
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White
                ),
                shadowElevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                onActiveChange = { searchActive = it },
                placeholder = {
                    Text(text = "Search", color = Color.Gray)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchActive)
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                if (searchText.isNotEmpty()) {
                                    searchText = ""
                                } else {
                                    searchActive = false
                                }
                            }
                        )
                }) {
            }
        }

        LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
            items(count =name.size) { item ->
                if (currentType.isEmpty()){
                    currentType= type.poll()!!
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp)
                    ) {
                        Text(
                            text = currentType,
                            color = colorResource(id = R.color.mainColor),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }else{
                    if (!name[item][0].toString().equals(currentType)){
                        currentType= type.poll()!!
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 25.dp)
                        ) {
                            Text(
                                text = currentType,
                                color = colorResource(id = R.color.mainColor),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable {
                            val intent = Intent(context,ChatActivity::class.java)
                            intent.putExtra("name",name[item%4])
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
                            Column(
                                modifier = Modifier
                                    .weight(0.6f)
                                    .padding(vertical = 20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.weight(0.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = name[item],
                                        color = colorResource(id = R.color.purple_700),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                Row(
                                    modifier = Modifier.weight(0.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "123456789",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(0.5f)
                                    )
                                }
                            }

                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
                    }
                }

            }
        }
    }
}

@Composable
fun PrivateComponent() {

}
