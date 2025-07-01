package com.zoozoostudio.filmscanner.view

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoozoostudio.filmscanner.R
import com.zoozoostudio.filmscanner.components.ModeSelector
import com.zoozoostudio.filmscanner.utils.ActivityLocalProvider
import com.zoozoostudio.filmscanner.utils.LocalColorCollection

@Composable
fun LighterPage() {
    val colorCollection = LocalColorCollection.current

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
                Box(
                    modifier = Modifier.size(13.dp, 13.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val radius = size.width / 2
                        drawCircle(
                            color = colorCollection.darkRed,
                            radius = radius,
                            center = center
                        )
                    }
                }
                Text(
                    text = "Connected",
                    fontSize = 20.sp,
                    color = colorCollection.lightBlue,
                )
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
            // will replace by camera view

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(colorCollection.black),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clickable(
                            onClick = {},
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        drawRoundRect(
                            color = colorCollection.lightBlue.copy(alpha = 0.1f),
                            size = size,
                            cornerRadius = CornerRadius(65f, 65f),
                        )
                    }
                    Icon(
                        painter = painterResource(R.drawable.remote_camera),
                        contentDescription = "remote camera",
                        tint = colorCollection.lightBlue
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun LighterPreviewPage() {
    ActivityLocalProvider {
        LighterPage()
    }
}
