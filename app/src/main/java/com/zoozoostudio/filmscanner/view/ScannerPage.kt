package com.zoozoostudio.filmscanner.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.zoozoostudio.filmscanner.R // Import your R file
import com.zoozoostudio.filmscanner.components.CameraSwitcher

@Composable
fun ScannerPage() {
    var sliderPosition by remember { mutableStateOf(.5f) }
    val orangeColor = colorResource(id = R.color.orange);
    val lightBlueColor = colorResource(id = R.color.light_blue);

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.black)),
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
                Box() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.clickable(
                            onClick = {
                                // 觸發一個浮動的下拉選單
                            },
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_down),
                            contentDescription = "",
                            tint = lightBlueColor,
                        )
                        Text(
                            fontSize = 20.sp,
                            text = "Scanner",
                            color = lightBlueColor
                        )
                    }
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { /* Handle dismiss if needed */ },
                        modifier = Modifier.background(color = colorResource(R.color.black))
                    ) {
                        DropdownMenuItem(
                            text = { Text("Scanner", color = lightBlueColor) },
                            onClick = { /* Handle Item 1 click */ }
                        )
                        DropdownMenuItem(
                            text = { Text("Lighter", color = lightBlueColor) },
                            onClick = { /* Handle Item 2 click */ }
                        )
                    }
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
                    tint = lightBlueColor,
                )
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    modifier = Modifier.weight(1f), // Allow Slider to take available space
                    colors = SliderDefaults.colors(
                        thumbColor = colorResource(R.color.orange), // Color of the thumb
                        activeTrackColor = colorResource(R.color.orange), // Color of the active track
                        inactiveTrackColor = colorResource(R.color.orange).copy(alpha = 0.24f) // Color of the inactive track (optional, with transparency)
                    )
                )
            }
            Row(
                modifier = Modifier
                    .background(color = lightBlueColor.copy(.1f))
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(16.dp)
                    .clickable(
                        onClick = {}
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.folder),
                        contentDescription = "folder",
                        tint = lightBlueColor,
                    )
                    Text(
                        fontSize = 20.sp,
                        text = "Folder",
                        color = lightBlueColor
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.arrow_right_circle),
                    contentDescription = "arrow",
                    tint = lightBlueColor,
                )
            }
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .clickable(
                            onClick = {}
                        ),
                ) {
                    val canvasRadius = size.width / 2
                    drawCircle(
                        color = orangeColor,
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
    ScannerPage()
}