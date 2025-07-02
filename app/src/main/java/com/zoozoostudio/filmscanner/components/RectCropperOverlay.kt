package com.zoozoostudio.filmscanner.components

import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.zoozoostudio.filmscanner.utils.ActivityLocalProvider
import com.zoozoostudio.filmscanner.utils.LocalColorCollection

@Composable
fun RectCropperOverlay (
    cropRect: Rect,
) {
    val colorCollection = LocalColorCollection.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val w = size.width
            val h = size.height

            drawPath(
                path = Path().apply {
                    fillType = PathFillType.EvenOdd
                    addRect(Rect(0f, 0f, w, h))
                    addRect(cropRect)
                },
                color = Color.Black.copy(alpha = 0.5f),
                style = Fill,
            )
            drawRect(
                topLeft = cropRect.topLeft,
                size = cropRect.size,
                color = colorCollection.blue,
                style = Stroke(4f),
            )
        }
    }
}


@Composable
@Preview
fun RectCropperPreview() {
    ActivityLocalProvider {
        RectCropperOverlay(
            Rect(50f, 50f, 100f, 100f)
        )
    }
}