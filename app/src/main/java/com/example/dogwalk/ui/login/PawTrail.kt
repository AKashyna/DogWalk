package com.example.dogwalk.ui.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.dogwalk.R
import kotlinx.coroutines.delay

@Composable
fun PawTrail(modifier: Modifier = Modifier) {
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    // Pętla sterująca restartem animacji
    var trigger by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                containerSize = it.size
            }
    ) {
        if (containerSize.height > 0) {
            val pawSpacingDp = 64.dp
            val pawSpacingPx = with(density) { pawSpacingDp.toPx() }
            val pawCount = (containerSize.height / pawSpacingPx).toInt()

            val delayBetween = 250L

            LaunchedEffect(trigger) {
                delay(pawCount * delayBetween + 1000)
                trigger++ // restart
            }

            for (i in 0 until pawCount) {
                var visible by remember(trigger) { mutableStateOf(false) }
                var alpha by remember(trigger) { mutableStateOf(1f) }

                LaunchedEffect(trigger) {
                    delay(i * delayBetween)
                    visible = true
                    delay(1000L)
                    alpha = 0f
                }

                if (visible) {
                    Image(
                        painter = painterResource(id = R.drawable.paw),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .offset(
                                x = (32 + (i % 2) * 16).dp,
                                y = with(density) { containerSize.height.toDp() } - pawSpacingDp * (i + 1)

                            )
                            .graphicsLayer {
                                this.alpha = alpha
                                rotationZ = if (i % 2 == 0) -25f else 25f
                            }
                    )
                }
            }
        }
    }
}
