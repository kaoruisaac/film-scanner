package com.zoozoostudio.filmscanner.view

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zoozoostudio.filmscanner.R // Import your R file
import com.zoozoostudio.filmscanner.components.CameraSwitcher
import com.zoozoostudio.filmscanner.components.ModeSelector
import com.zoozoostudio.filmscanner.components.NegativeCameraPreview
import com.zoozoostudio.filmscanner.components.PathSelector
import com.zoozoostudio.filmscanner.components.RectCropperOverlay
import com.zoozoostudio.filmscanner.utils.ActivityLocalProvider
import com.zoozoostudio.filmscanner.utils.LocalBoundCamera
import com.zoozoostudio.filmscanner.utils.LocalColorCollection
import androidx.core.net.toUri
import com.zoozoostudio.filmscanner.components.ShutterMask

@Composable
fun ScannerPage() {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current
    var whiteBalance by remember { mutableFloatStateOf(.5f) }
    var cropRect by remember { mutableStateOf(Rect.Zero) }
    var frameRect by remember { mutableStateOf(Rect.Zero) }
    var outputUri by remember {
        mutableStateOf<Uri?>(
                if (isPreview) {
                    Uri.Builder().apply {
                        appendPath("/preview/path/of/Pictures")
                    }.build()
                } else {
                    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                    sharedPreferences.getString("outputUri", null)?.toUri()
                },
        )
    }
    val colorCollection = LocalColorCollection.current
    val boundCamera = LocalBoundCamera.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorCollection.black),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CameraSwitcher()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ModeSelector()
                Icon(
                    painter = painterResource(R.drawable.setting),
                    contentDescription = "settings",
                    tint = colorCollection.lightBlue,
                    modifier = Modifier.clickable(
                        onClick = {}
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(colorCollection.pureBlack)
                .onSizeChanged { box ->
                    val h = box.height
                    val w = box.width
                    val defaultCropRectHeight = h * 0.9f
                    val defaultCropRectWidth = defaultCropRectHeight / 1.5f
                    val cropRectOffsetX = (w - defaultCropRectWidth) / 2
                    val cropRectOffsetY = (h - defaultCropRectHeight) / 2
                    cropRect = Rect(
                        offset = Offset(cropRectOffsetX, cropRectOffsetY),
                        size = Size(defaultCropRectWidth, defaultCropRectHeight)
                    )
                    frameRect = Rect(
                        offset = Offset(0f, 0f),
                        size = Size(w.toFloat(), h.toFloat())
                    )
                }
        ) {
            ShutterMask.Content {
                NegativeCameraPreview(whiteBalance)
                RectCropperOverlay(
                    cropRect
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .height(90.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.temperature),
                    contentDescription = "temperature",
                    tint = colorCollection.lightBlue,
                )
                Slider(
                    value = whiteBalance,
                    onValueChange = { whiteBalance = it },
                    modifier = Modifier.weight(1f), // Allow Slider to take available space
                    colors = SliderDefaults.colors(
                        thumbColor = colorCollection.orange, // Color of the thumb
                        activeTrackColor = colorCollection.orange, // Color of the active track
                        inactiveTrackColor = colorCollection.orange.copy(alpha = 0.24f) // Color of the inactive track (optional, with transparency)
                    )
                )
            }
            PathSelector(
                outputUri,
                onChange = { outputUri = it }
            )
            Box(
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .clickable(
                            onClick = {
                                if (outputUri != null) {
                                    try {
                                        ShutterMask.shut()
                                        boundCamera.takePicture(
                                            cropRect,
                                            frameRect,
                                            outputUri = outputUri!!,
                                            whiteBalance,
                                            context,
                                        )
                                        ShutterMask.success()
                                    } catch (e: Exception) {
                                        ShutterMask.failed(context)
                                    }
                                }
                            }
                        ),
                ) {
                    val canvasRadius = size.width / 2
                    drawCircle(
                        color = colorCollection.orange,
                        center = Offset(x = canvasRadius, y = canvasRadius),
                        radius = canvasRadius,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ScannerPagePreview() {
    ActivityLocalProvider {
        ScannerPage()
    }
}