package com.zoozoostudio.filmscanner.components

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.util.Log
import android.view.ScaleGestureDetector
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.zoozoostudio.filmscanner.utils.LocalBoundCamera

@SuppressLint("ClickableViewAccessibility")
@Composable
fun NegativeCameraPreview(
    whiteBalance: Float,
) {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val boundCamera = LocalBoundCamera.current


    // 用 remember 保存 PreviewView 的實例
    var previewView: PreviewView? by remember { mutableStateOf(null) }

    if (!isPreview) {
        // Lifecycle-aware 的綁定／解绑
        DisposableEffect(previewView) {
            // 1. 取得 CameraProvider
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            var cameraProvider: ProcessCameraProvider? = null
            val imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()
            val cameraExecutor = ContextCompat.getMainExecutor(context)

            // 2. 當 PreviewView 已就緒，就綁定到 lifecycle
            previewView?.let { pv ->
                cameraProviderFuture.addListener({
                    cameraProvider = cameraProviderFuture.get().also { provider ->
                        // 先解绑所有舊用例，確保乾淨
                        provider.unbindAll()

                        // 建立 Preview UseCase
                        val previewUseCase = Preview.Builder()
                            .build()
                            .also { it.setSurfaceProvider(pv.surfaceProvider) }

                        // 綁定到當前 activity lifecycle
                        val camera = provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            previewUseCase,
                            imageCapture,
                        )
                        boundCamera.setCamera(
                            camera.cameraControl,
                            camera.cameraInfo,
                            imageCapture,
                            cameraExecutor,
                        )
                    }
                }, cameraExecutor)
            }

            onDispose {
                // Composable 拆除時，取消所有用例綁定
                cameraProvider?.unbindAll()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isPreview) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        previewView = this
                        this.scaleType = PreviewView.ScaleType.FIT_CENTER
                        this.setBackgroundColor(0xFFC7C7C7.toInt())
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE

                        // 兩指縮放
                        val scaleDetector = ScaleGestureDetector(ctx, object: ScaleGestureDetector.SimpleOnScaleGestureListener() {
                            override fun onScale(detector: ScaleGestureDetector): Boolean {
                                val current = boundCamera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
                                val nz = (current * detector.scaleFactor)
                                    .coerceIn(boundCamera.minZoomRatio.value, boundCamera.maxZoomRatio.value)
                                boundCamera.setZoomRatio(nz)
                                return true
                            }
                        })
                        setOnTouchListener { _, ev ->
                            scaleDetector.onTouchEvent(ev)
                            true
                        }
                    }
                },
                update = { view ->
                    // 1. 負片濾鏡
                    val negativeColorMatrix = ColorMatrix(floatArrayOf(
                        -1f,  0f,  0f, 0f, 255f,
                        0f, -1f,  0f, 0f, 255f,
                        0f,  0f, -1f, 0f, 255f,
                        0f,  0f,  0f, 1f,   0f
                    ))

                    // 2. 白平衡調整：whiteBalance range 0f..1f
                    //    whiteBalance = 0.5 時 rScale/bScale = 1 (中性)
                    val delta = (whiteBalance - 0.5f) * 2f    // -1..1
                    val rScale = 1f + delta * 0.2f           // ±20% 之間
                    val bScale = 1f - delta * 0.2f
                    val wbMat = ColorMatrix().apply {
                        setScale(rScale, 1f, bScale, 1f)
                    }

                    // 3. 先套白平衡，再套負片
                    negativeColorMatrix.preConcat(wbMat)

                    // 4. 套到 PreviewView 上
                    view.setRenderEffect(
                        RenderEffect.createColorFilterEffect(
                            ColorMatrixColorFilter(negativeColorMatrix)
                        )
                    )
                }
            )
        }
    }
}