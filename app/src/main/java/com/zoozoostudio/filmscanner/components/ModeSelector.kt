package com.zoozoostudio.filmscanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoozoostudio.filmscanner.R
import com.zoozoostudio.filmscanner.utils.ActivityLocalProvider
import com.zoozoostudio.filmscanner.utils.AppModeState
import com.zoozoostudio.filmscanner.utils.LocalAppModeState
import com.zoozoostudio.filmscanner.utils.LocalColorCollection

@Composable
fun ModeSelector () {
    val colorCollection = LocalColorCollection.current
    val appModeState = LocalAppModeState.current
    var isOpen by remember { mutableStateOf(false) }

    Box() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.clickable(
                onClick = {
                    isOpen = !isOpen
                },
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_down),
                contentDescription = "",
                tint = colorCollection.lightBlue,
            )
            Text(
                fontSize = 20.sp,
                text = if (appModeState.value == AppModeState.SCANNER) "Scanner" else "Lighter",
                color = colorCollection.lightBlue
            )
        }
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = { isOpen = false },
            modifier = Modifier.background(colorCollection.black)
        ) {
            DropdownMenuItem(
                text = { Text("Scanner", color = colorCollection.lightBlue) },
                onClick = {
                    appModeState.value = AppModeState.SCANNER
                    isOpen = false
                }
            )
            DropdownMenuItem(
                text = { Text("Lighter", color = colorCollection.lightBlue) },
                onClick = {
                    appModeState.value = AppModeState.LIGHTER
                    isOpen = false
                }
            )
        }
    }
}

@Composable
@Preview
fun ModeSelectorPreview() {
    ActivityLocalProvider {
        ModeSelector()
    }
}