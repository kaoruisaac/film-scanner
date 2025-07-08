package com.zoozoostudio.filmscanner.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoozoostudio.filmscanner.R
import com.zoozoostudio.filmscanner.utils.LocalColorCollection
import java.nio.file.Paths
import kotlin.io.path.name
import androidx.core.content.edit

@Composable
fun PathSelector(
    outputUri: Uri?,
    onChange: (Uri) -> Unit
) {
    val colorCollection = LocalColorCollection.current
    val configuration = LocalConfiguration.current // Get the current configuration
    val context = LocalContext.current

    // 1. 建立一個 Launcher 來啟動資料夾選取
    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            // 2. 權限永續保存 (optional)
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            onChange(uri)
            val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            sharedPreferences.edit { putString("outputUri", uri.toString()) }
        }
    }
    val outputPathName = if (outputUri == null) "Please select a folder" else Paths.get(outputUri.path).name
    Row(
        modifier = Modifier
            .background(color = colorCollection.lightBlue.copy(.1f))
            .height(70.dp)
            .padding(16.dp)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    folderPickerLauncher.launch(null)
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.folder),
                contentDescription = "folder",
                tint = colorCollection.lightBlue,
            )
            Text(
                fontSize = 20.sp,
                text = outputPathName,
                color = colorCollection.lightBlue,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.width((configuration.screenWidthDp - 122).dp)
            )
        }
        Icon(
            painter = painterResource(R.drawable.arrow_right_circle),
            contentDescription = "arrow",
            tint = colorCollection.lightBlue,
        )
    }
}