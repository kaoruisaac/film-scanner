package com.zoozoostudio.filmscanner.utils

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

fun getBackFacingCameraIds(context: Context): List<String> {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraIds = mutableListOf<String>()

    try {
        for (cameraId in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                cameraIds.add(cameraId)
            }
        }
    } catch (e: Exception) {
        // Handle camera access exceptions
        e.printStackTrace()
    }
    return cameraIds
}