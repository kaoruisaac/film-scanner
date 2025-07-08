package com.zoozoostudio.filmscanner.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LighterBox() {
    var scale by remember { mutableFloatStateOf(1f) }
    var boxOffset by remember { mutableStateOf<Offset?>(null) }

    Canvas(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                }
            }
            .pointerInput(Unit) {
                var diffOffset = Offset.Zero
                var originalOffset = boxOffset?.copy() ?: Offset.Zero
                detectDragGestures(
                    onDragStart = {
                        originalOffset = boxOffset?.copy() ?: Offset.Zero
                    },
                    onDrag = { _, offset ->
                        diffOffset += offset
                        boxOffset = originalOffset + diffOffset
                        Log.d("LighterBox", "onDrag: $diffOffset")
                    },
                    onDragEnd = {
                        diffOffset = Offset.Zero
                    },
                )
            }
            .onSizeChanged { size ->
                if (boxOffset == null) {
                    boxOffset = Offset(
                        size.width / 2 - 300f * scale / 2,
                        size.height / 2 - 450f * scale / 2
                    )
                }
            }
    ) {
        val whiteRect = Rect(
            size = Size(300f * scale, 450f * scale),
            offset = boxOffset ?: Offset.Zero,
        )
        drawRect(
            color = androidx.compose.ui.graphics.Color.White,
            size = whiteRect.size,
            topLeft = whiteRect.topLeft
        )
    }
}

@Composable
@Preview
fun LighterBoxPreview() {
    LighterBox()
}