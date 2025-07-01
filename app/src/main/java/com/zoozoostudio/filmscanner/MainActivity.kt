package com.zoozoostudio.filmscanner

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zoozoostudio.filmscanner.ui.theme.FilmScannerTheme
import com.zoozoostudio.filmscanner.utils.ActivityLocalProvider
import com.zoozoostudio.filmscanner.utils.AppModeState
import com.zoozoostudio.filmscanner.utils.LocalAppModeState
import com.zoozoostudio.filmscanner.utils.LocalColorCollection
import com.zoozoostudio.filmscanner.view.LighterPage
import com.zoozoostudio.filmscanner.view.ScannerPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        setContent {
            ActivityLocalProvider {
                val colorCollection = LocalColorCollection.current
                val appModeState = LocalAppModeState.current
                Scaffold() { innerPadding ->
                    FilmScannerTheme {
                        Box(modifier = Modifier
                            .background(colorCollection.black)
                            .padding(innerPadding)
                        ) {
                            if (appModeState.value == AppModeState.SCANNER) {
                                ScannerPage()
                            } else {
                                LighterPage()
                            }
                        }
                    }
                }
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 權限已授予，可以啟動相機
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ActivityLocalProvider {
        FilmScannerTheme {
            ScannerPage()
        }
    }
}