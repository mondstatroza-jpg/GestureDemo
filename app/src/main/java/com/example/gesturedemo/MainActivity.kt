package com.example.gesturedemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
    GestureImageDemo(modifier)
}

@Composable
fun GestureImageDemo(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Текущий режим жеста
    var currentMode by remember { mutableStateOf("tap") }

    // Смещение, масштаб и угол
    var xOffset by remember { mutableStateOf(0f) }
    var yOffset by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }
    var angle by remember { mutableStateOf(0f) }

    // Уведомление
    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    // Состояние для масштаба (отдельно)
    val scaleState = rememberTransformableState { scaleChange, _, _ ->
        scale *= scaleChange
    }

    // Состояние для вращения (отдельно)
    val rotateState = rememberTransformableState { _, _, rotationChange ->
        angle += rotationChange
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // КНОПКИ ВЫБОРА РЕЖИМА
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { currentMode = "tap" }) { Text("Клик") }
            Button(onClick = { currentMode = "press" }) { Text("Нажатия") }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { currentMode = "drag" }) { Text("Перетаск.") }
            Button(onClick = { currentMode = "scroll" }) { Text("Прокрутка") }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { currentMode = "scale" }) { Text("Масштаб") }
            Button(onClick = { currentMode = "rotate" }) { Text("Вращение") }
        }

        // Подпись текущего режима
        Text(
            text = "Режим: $currentMode",
            modifier = Modifier.padding(8.dp)
        )

        // ОБЛАСТЬ С КАРТИНКОЙ
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Базовый модификатор для картинки
            var imageModifier: Modifier = Modifier
                .size(200.dp)
                .offset { IntOffset(xOffset.roundToInt(), yOffset.roundToInt()) }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = angle
                )

            // Добавляем жест в зависимости от выбранного режима
            imageModifier = when (currentMode) {
                "tap" -> imageModifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { showToast("Простой клик!") }
                    )
                }

                "press" -> imageModifier.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { showToast("onPress (касание)") },
                        onDoubleTap = { showToast("onDoubleTap (двойной тап)") },
                        onLongPress = { showToast("onLongPress (долгое нажатие)") },
                        onTap = { showToast("onTap (простой тап)") }
                    )
                }

                "drag" -> imageModifier.pointerInput(Unit) {
                    detectDragGestures { _, distance ->
                        xOffset += distance.x
                        yOffset += distance.y
                    }
                }

                "scroll" -> imageModifier.scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollableState { distance ->
                        yOffset += distance
                        distance
                    }
                )

                "scale" -> imageModifier.transformable(state = scaleState)

                "rotate" -> imageModifier.transformable(state = rotateState)

                else -> imageModifier
            }

            Image(
                painter = painterResource(id = R.drawable.vacation),
                contentDescription = "Vacation",
                modifier = imageModifier
            )
        }
    }
}