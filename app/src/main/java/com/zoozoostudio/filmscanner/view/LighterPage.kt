package com.zoozoostudio.filmscanner.view

import android.view.WindowManager
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoozoostudio.filmscanner.R
import com.zoozoostudio.filmscanner.components.LighterBox
import com.zoozoostudio.filmscanner.components.ModeSelector
import com.zoozoostudio.filmscanner.utils.ActivityLocalProvider
import com.zoozoostudio.filmscanner.utils.LocalColorCollection
import com.zoozoostudio.filmscanner.utils.findActivity

@Composable
fun LighterPage() {
    val colorCollection = LocalColorCollection.current
    val context = LocalContext.current
    var originalBrightness by remember { mutableStateOf<Float?>(null) }

//    DisposableEffect(Unit) {
//        val window = context.findActivity()?.window
//        window?.let {
//            val layoutParams = it.attributes
//            originalBrightness = layoutParams.screenBrightness // 儲存原始亮度
//            layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL // 設定為最大亮度
//            it.attributes = layoutParams
//        }
//
//        onDispose {
//            window?.let {
//                val layoutParams = it.attributes
//                // 還原原始亮度，如果之前有儲存的話
//                originalBrightness?.let { brightness ->
//                    layoutParams.screenBrightness = brightness
//                } ?: run {
//                    // 如果沒有儲存原始亮度（例如在模擬器或特定情況下），則還原為系統預設值
//                    layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
//                }
//                it.attributes = layoutParams
//            }
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorCollection.black),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
//                Box(
//                    modifier = Modifier.size(13.dp, 13.dp)
//                ) {
//                    Canvas(modifier = Modifier.fillMaxSize()) {
//                        val radius = size.width / 2
//                        drawCircle(
//                            color = colorCollection.darkRed,
//                            radius = radius,
//                            center = center
//                        )
//                    }
//                }
//                Text(
//                    text = "Connected",
//                    fontSize = 20.sp,
//                    color = colorCollection.lightBlue,
//                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                .background(colorCollection.black)
        ) {
            // will replace by lighter view
            LighterBox()
        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(140.dp)
//                .background(colorCollection.black),
//            contentAlignment = Alignment.Center,
//        ) {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clickable(
//                            onClick = {},
//                        ),
//                    contentAlignment = Alignment.Center,
//                ) {
//                    Canvas(
//                        modifier = Modifier
//                            .fillMaxSize()
//                    ) {
//                        drawRoundRect(
//                            color = colorCollection.lightBlue.copy(alpha = 0.1f),
//                            size = size,
//                            cornerRadius = CornerRadius(65f, 65f),
//                        )
//                    }
//                    Icon(
//                        painter = painterResource(R.drawable.remote_camera),
//                        contentDescription = "remote camera",
//                        tint = colorCollection.lightBlue
//                    )
//                }
//            }
//        }
    }
}

@Composable
@Preview
fun LighterPreviewPage() {
    ActivityLocalProvider {
        LighterPage()
    }
}
