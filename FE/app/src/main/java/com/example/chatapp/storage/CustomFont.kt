package com.example.chatapp.storage

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.chatapp.R

object CustomFont {
    val font = FontFamily(
        Font(R.font.bevietnampro_regular, FontWeight.Normal),
        Font(R.font.bevietnamepro_bold, FontWeight.Bold)
    )
}