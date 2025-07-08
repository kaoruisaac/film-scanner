package com.zoozoostudio.filmscanner.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.zoozoostudio.filmscanner.R
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.io.path.pathString
import androidx.core.graphics.createBitmap
import java.nio.file.Paths
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile

val sdf = SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault())
enum class AppModeState {
    SCANNER, LIGHTER
}
val LocalAppModeState = compositionLocalOf<MutableState<AppModeState>> { error("No AppModeState provided") }
val LocalColorCollection = compositionLocalOf<ColorCollection> { error("No ColorCollection provided") }
class ColorCollection {
    var lightBlue: Color = Color.Unspecified;
    var blue: Color = Color.Unspecified;
    var orange: Color = Color.Unspecified;
    var black: Color = Color.Unspecified;
    var pureBlack: Color = Color.Unspecified;
    var green: Color = Color.Unspecified;
    var darkRed: Color = Color.Unspecified;

    @Composable
    fun CollectColors () {
        lightBlue = colorResource(id = R.color.light_blue)
        blue = colorResource(id = R.color.blue)
        orange = colorResource(id = R.color.orange)
        black = colorResource(id = R.color.black)
        pureBlack = colorResource(id = R.color.pure_black)
        green = colorResource(id = R.color.green)
        darkRed = colorResource(id = R.color.dark_red)
    }
}

class BoundCameraProvider {
    lateinit var cameraControl: CameraControl
    lateinit var cameraInfo: CameraInfo
    lateinit var imageCapture: ImageCapture
    lateinit var cameraExecutor: Executor
    var maxZoomRatio: MutableState<Float> = mutableFloatStateOf(1f)
    var minZoomRatio: MutableState<Float> = mutableFloatStateOf(1f)
    var currentZoomRatio: MutableState<Float> = mutableFloatStateOf(1f)
    var ratioList: MutableState<List<Float>> = mutableStateOf(listOf(1f))
    var ratioIndex: MutableState<Int> = mutableStateOf(0)
    fun setZoomRatioBoundaries(min: Float?, max: Float?) {
        val minValue = min ?: 1f
        val maxValue = max ?: 1f
        this.minZoomRatio.value = minValue
        this.maxZoomRatio.value = maxValue
        val newRatioList = mutableListOf<Float>()
        if (minValue < 1) newRatioList.add(minValue)
        newRatioList.add(1f)
        if (maxValue >= 2) newRatioList.add(2f)
        if (maxValue >= 3) newRatioList.add(3f)
        if (maxValue >= 5) newRatioList.add(5f)
        if (maxValue > 5) newRatioList.add(maxValue)
        ratioList.value = newRatioList.toList()
    }
    fun setCamera(
        cameraControl: CameraControl,
        cameraInfo: CameraInfo,
        imageCapture: ImageCapture,
        cameraExecutor: Executor,
    ) {
        this.cameraControl = cameraControl
        this.cameraInfo = cameraInfo
        this.imageCapture = imageCapture
        this.cameraExecutor = cameraExecutor
        setZoomRatioBoundaries(
            cameraInfo.zoomState.value?.minZoomRatio,
            cameraInfo.zoomState.value?.maxZoomRatio,
        )
        setZoomRatio(1f)
    }
    fun setZoomRatio(ratio: Float) {
        cameraControl.setZoomRatio(ratio)
        currentZoomRatio.value = ratio
        val index = ratioList.value.indexOfFirst {
            currentRatio -> ratio <= currentRatio
        }
        ratioIndex.value = if (index != -1) index else ratioList.value.size -1
    }
    fun takePicture(cropRect: Rect, frameRect: Rect, outputUri: Uri, whiteBalance: Float, context: Context) {
        val contentResolver = context.contentResolver
        imageCapture.takePicture(cameraExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                // 如果bitmap為橫式則把bitmap旋轉成直式
                if (bitmap.width > bitmap.height) {
                    val matrix = Matrix()
                    matrix.postRotate(90f) // 順時針旋轉90度
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                }

                val imageHeight = bitmap.height.toFloat()
                val imageWidth = bitmap.width.toFloat()
                val scale = imageHeight / frameRect.height

                val finalCropHeight = cropRect.size.height * scale
                val finalCropWidth = cropRect.size.width * scale
                val finalCropTop = ((imageHeight - finalCropHeight) / 2)
                val finalCropLeft = ((imageWidth - finalCropWidth) / 2)

                val croppedBitmap = Bitmap.createBitmap(
                    bitmap,
                    finalCropLeft.toInt(),
                    finalCropTop.toInt(),
                    finalCropWidth.toInt(),
                    finalCropHeight.toInt(),
                )

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

                // 創建 ColorMatrixColorFilter
                val colorFilter = ColorMatrixColorFilter(negativeColorMatrix)

                // 創建一個新的 Bitmap 來繪製帶有濾鏡的效果
                val invertedBitmap = createBitmap(croppedBitmap.width, croppedBitmap.height, croppedBitmap.config ?: Bitmap.Config.HARDWARE)

                // 創建 Canvas 和 Paint
                val canvas = Canvas(invertedBitmap)
                val paint = Paint()
                paint.colorFilter = colorFilter // 應用 ColorFilter
                canvas.drawBitmap(croppedBitmap, 0f, 0f, paint)

                try {
                    val fileName = "${sdf.format(Date())}.jpg"
                    val directory = DocumentFile.fromTreeUri(context, outputUri)
                    directory?.let {
                        val newFile = directory.createFile("image/jpeg",fileName)
                        newFile?.let {
                            context.contentResolver.openOutputStream(newFile.uri)?.use { outputStream ->
                                invertedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("FileSave", "Error saving file", e)
                    e.printStackTrace()
                    throw e
                } finally {
                    image.close()
                    bitmap.recycle()
                    croppedBitmap.recycle()
                    invertedBitmap.recycle()
                }

            }
        })
    }
}

val LocalBoundCamera = compositionLocalOf<BoundCameraProvider> { error("No cameraControl provided") }
@Composable
fun ActivityLocalProvider(myapp: @Composable () -> Unit = {}) {
    val appModeState = remember { mutableStateOf(AppModeState.SCANNER) }
    val colorCollection = ColorCollection()
    val boundCamera = BoundCameraProvider()
    colorCollection.CollectColors()
    CompositionLocalProvider(
        LocalAppModeState provides appModeState,
        LocalColorCollection provides colorCollection,
        LocalBoundCamera provides boundCamera
    ) {
        myapp()
    }
}