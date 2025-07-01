package com.zoozoostudio.filmscanner.components

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.view.ScaleGestureDetector
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.zoozoostudio.filmscanner.utils.LocalBoundCamera

@SuppressLint("ClickableViewAccessibility")
@Composable
fun NegativeCameraPreview() {
    val isPreview = LocalInspectionMode.current
    val boundCamera = LocalBoundCamera.current

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isPreview) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    // 建立 PreviewView
                    PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE

                        // 套用負片色彩矩陣
                        val cm = ColorMatrix(floatArrayOf(
                            -1f,  0f,  0f, 0f, 255f,
                            0f, -1f,  0f, 0f, 255f,
                            0f,  0f, -1f, 0f, 255f,
                            0f,  0f,  0f, 1f,   0f
                        ))
                        setRenderEffect(
                            RenderEffect.createColorFilterEffect(
                                ColorMatrixColorFilter(cm)
                            )
                        )

                        // 這裡先建立 ScaleGestureDetector，處理兩指縮放
                        val scaleGestureDetector = ScaleGestureDetector(ctx, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                            override fun onScale(detector: ScaleGestureDetector): Boolean {
                                // 讀當前 zoomRatio、計算新值、並限制在 min~max 之間
                                val currentZoom = boundCamera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
                                val newZoom = (currentZoom * detector.scaleFactor)
                                    .coerceIn(
                                        boundCamera.minZoomRatio.value,
                                        boundCamera.maxZoomRatio.value
                                    )
                                boundCamera.setZoomRatio(newZoom)
                                return true
                            }
                        })

                        // 把手勢事件交給 scaleDetector 處理
                        setOnTouchListener { _, event ->
                            scaleGestureDetector.onTouchEvent(event)
                            true
                        }

                        // 綁定 CameraX
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val previewUseCase = Preview.Builder()
                                .build()
                                .also { it.setSurfaceProvider(surfaceProvider) }
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            cameraProvider.unbindAll()
                            val camera = cameraProvider.bindToLifecycle(
                                ctx as ComponentActivity,
                                cameraSelector,
                                previewUseCase
                            )
                            // 將 cameraControl、cameraInfo 存進 LocalBoundCamera
                            boundCamera.setCamera(camera.cameraControl, camera.cameraInfo)
                        }, ContextCompat.getMainExecutor(ctx))
                    }
                },
                update = { /* 如需動態切換效果，可在此更新 */ }
            )
        }
    }
}