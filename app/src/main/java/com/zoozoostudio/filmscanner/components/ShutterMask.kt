package com.zoozoostudio.filmscanner.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoozoostudio.filmscanner.utils.ActivityLocalProvider
import com.zoozoostudio.filmscanner.utils.LocalColorCollection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val myScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
object ShutterMask {
    private var _isLoading = mutableStateOf(false)
    private var _isSuccess = mutableStateOf(false)

    fun shut() {
        myScope.launch {
            _isLoading.value = true
            delay(300)
            _isLoading.value = false
        }
    }

    fun success() {
        myScope.launch {
            _isSuccess.value = true
            delay(2000)
            _isSuccess.value = false
        }
    }

    fun failed(context: Context) {
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun Content(
        modifier: Modifier? = Modifier,
        child: @Composable () -> Unit = {},
    ) {
        val colorCollection = LocalColorCollection.current
        val isPreview = LocalInspectionMode.current

        // Animate the background color
        val backgroundColor by animateColorAsState(
            targetValue = if (_isLoading.value) Color.Black.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0f),
            animationSpec = tween(durationMillis = 300) // Adjust duration as needed
        )

        Box(
            modifier = (modifier ?: Modifier)
                .fillMaxSize()
        ) {
            child()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .clipToBounds(),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = isPreview || _isSuccess.value,
                    enter = slideInVertically(
                        initialOffsetY = { it + 150 },
                        animationSpec = tween(durationMillis = 500)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it + 150 },
                        animationSpec = tween(durationMillis = 500)
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(0.dp, -30.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorCollection.orange.copy(alpha = 0.7f))
                    ) {
                        Text(
                            "Film Saved",
                            modifier = Modifier.padding(20.dp, 8.dp),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun ShutterMaskPreview() {
    ActivityLocalProvider {
        ShutterMask.Content {}
    }
}
