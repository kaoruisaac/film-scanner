package com.zoozoostudio.filmscanner.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zoozoostudio.filmscanner.R
import com.zoozoostudio.filmscanner.utils.ActivityLocalProvider
import com.zoozoostudio.filmscanner.utils.LocalBoundCamera
import com.zoozoostudio.filmscanner.utils.LocalColorCollection
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

@Composable
fun CameraSwitcher() {
    val colorCollection = LocalColorCollection.current
    val boundCamera = LocalBoundCamera.current
    val ratioList = if (LocalInspectionMode.current)
        listOf(0.6f, 1f, 2f)
    else
        boundCamera.ratioList.value

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .clickable(
                onClick = {
                    val newIndex = if (boundCamera.ratioIndex.value + 1 < ratioList.size) boundCamera.ratioIndex.value + 1 else 0
                    boundCamera.setZoomRatio(ratioList.get(newIndex))
                }
            )
            .pointerInput(Unit) {
                var dragOffset = 0f
                var currentIndex = boundCamera.ratioIndex.value
                detectHorizontalDragGestures(
                    onDragStart = {
                        dragOffset = 0f
                        currentIndex = boundCamera.ratioIndex.value
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        // 拖曳量為正：向右滑，放大；為負：向左滑，縮小
                        dragOffset += dragAmount

                        val newIndex =(currentIndex + round(dragOffset / 150).toInt()).coerceIn(0, boundCamera.ratioList.value.size - 1)
                        Log.d("dragAmount", "dragOffset=${dragOffset} index=${floor(dragOffset / 100)}")

                        if (newIndex != currentIndex) {
                            boundCamera.setZoomRatio(ratioList[newIndex])
                            currentIndex = newIndex
                            dragOffset = 0f
                        }
                    }
                )
            }
    ) {
        Icon(
            painter = painterResource(R.drawable.camera_switch),
            contentDescription = "Switch Camera",
            tint = colorCollection.lightBlue,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ratioList.forEachIndexed { index, _ ->
                Icon(
                    painter = painterResource(
                        if (index == boundCamera.ratioIndex.value)
                            R.drawable.dot else R.drawable.dot_outline
                    ),
                    contentDescription = null,
                    tint = colorCollection.lightBlue,
                )
            }
        }
    }
}

@Composable
@Preview
fun CameraSwitcherPreview() {
    ActivityLocalProvider {
        CameraSwitcher()
    }
}
