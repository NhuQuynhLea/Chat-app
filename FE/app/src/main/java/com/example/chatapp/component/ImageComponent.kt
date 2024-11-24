@file:OptIn(ExperimentalLayoutApi::class)

package com.example.chatapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.storage.CustomFont

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageComponent(){
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(5) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp)){
                Text(
                    text = "Ngày 24/10/2024",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                    , fontFamily = CustomFont.font
                )
            }
            FlowRow(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.meme),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size((screenWidth - 60.dp) / 3f)
                        .clip(RoundedCornerShape(20.dp))
                )
                Image(
                    painter = painterResource(id = R.drawable.meme),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size((screenWidth - 60.dp) / 3f)
                        .clip(RoundedCornerShape(20.dp))
                )
                Image(
                    painter = painterResource(id = R.drawable.meme),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size((screenWidth - 60.dp) / 3f)
                        .clip(RoundedCornerShape(20.dp))
                )
                Image(
                    painter = painterResource(id = R.drawable.meme),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size((screenWidth - 60.dp) / 3f)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
        }
    }
}