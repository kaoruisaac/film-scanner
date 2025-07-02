package com.zoozoostudio.filmscanner.utils

import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.view.CameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.zoozoostudio.filmscanner.R

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
    fun setCamera(cameraControl: CameraControl, cameraInfo: CameraInfo) {
        this.cameraControl = cameraControl
        this.cameraInfo = cameraInfo
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