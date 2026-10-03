package com.example.gesturedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    ScrollModifiers(modifier)
}

@Composable
fun ScrollModifiers(modifier: Modifier = Modifier) {
    // Смещения картинки
    var xOffset by remember { mutableStateOf(0f) }
    var yOffset by remember { mutableStateOf(0f) }

    // Размер видимого окна — 150x150
    Box(
        modifier = modifier
            .size(150.dp)
            .pointerInput(Unit) {
                detectDragGestures { _, distance ->
                    xOffset += distance.x
                    yOffset += distance.y
                }
            }
    ) {
        // Картинка
        Image(
            painter = painterResource(id = R.drawable.vacation),
            contentDescription = "Vacation",
            modifier = Modifier
                .size(360.dp, 270.dp)
                .offset { IntOffset(xOffset.roundToInt(), yOffset.roundToInt()) }
        )
    }
}