package com.zoozoostudio.filmscanner.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zoozoostudio.filmscanner.R
import com.zoozoostudio.filmscanner.utils.getBackFacingCameraIds

@Composable
fun CameraSwitcher (cameraId: String = "fake-camera", onChanged: (String) -> Unit = {}) {
    val lightBlueColor = colorResource(id = R.color.light_blue)
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val backCameraIds = remember { if (isPreview) listOf("fake-camera", "fake-camera-02", "fake-camera-03") else getBackFacingCameraIds(context) }
    val currentIndex = remember { backCameraIds.indexOfFirst { it == cameraId } }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.clickable(
            onClick = {
                val newIndex = if (currentIndex == backCameraIds.size - 1) 0 else currentIndex + 1
                onChanged(backCameraIds[newIndex])
            }
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.camera_switch),
            contentDescription = "Switch Camera",
            tint = lightBlueColor,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (backCameraIds.isNotEmpty()) {
                backCameraIds.forEachIndexed { index, cId ->
                    Icon(
                        painter = if (cId == cameraId)
                            painterResource(R.drawable.dot) else painterResource(R.drawable.dot_outline),
                        contentDescription = "",
                        tint = lightBlueColor,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun CameraSwitcherPreview () {
    CameraSwitcher()
}