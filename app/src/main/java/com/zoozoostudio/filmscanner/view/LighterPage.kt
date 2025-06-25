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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoozoostudio.filmscanner.R

@Composable
fun LighterPage() {
    val darkRedColor = colorResource(R.color.dark_red);
    val lightBlueColor = colorResource(R.color.light_blue);
    val opacityWhiteColor = colorResource(R.color.white).copy(alpha = 0.1f);

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.black)),
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
                            color = darkRedColor,
                            radius = radius,
                            center = center
                        )
                    }
                }
                Text(
                    text = "Connected",
                    fontSize = 20.sp,
                    color = lightBlueColor,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_down),
                        contentDescription = "",
                        tint = lightBlueColor,
                    )
                    Text(
                        fontSize = 20.sp,
                        text = "Lighter",
                        color = lightBlueColor
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.setting),
                    contentDescription = "settings",
                    tint = lightBlueColor,
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
                .background(colorResource(R.color.pure_black))
        ) {
            // will replace by camera view

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(color = colorResource(R.color.black)),
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
                            color = opacityWhiteColor,
                            size = size,
                            cornerRadius = CornerRadius(65f, 65f),
                        )
                    }
                    Icon(
                        painter = painterResource(R.drawable.remote_camera),
                        contentDescription = "remote camera",
                        tint = colorResource(R.color.light_blue)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun LighterPreviewPage() {
    LighterPage()
}
