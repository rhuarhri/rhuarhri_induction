package com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class ErrorMessageWidget {

    @Composable
    fun errorMessage(message : String) {

        val currentState = if (message.isNotBlank()) {
            ErrorMessageState.SHOW
        } else {
            ErrorMessageState.HIDE
        }

        val errorMessageAnimation = errorMessageAnimation(state = currentState)

        Box(
            Modifier
                .height(40.dp)
                .fillMaxWidth()
                .alpha(errorMessageAnimation.alpha.value)) {
            Row {
                Icon(Icons.Rounded.Warning, contentDescription = "error", tint = Color.Red)
                Text(message, color = Color.Red)
            }
        }
    }

    @Composable
    fun errorMessageAnimation(state : ErrorMessageState) : ErrorMessageAnimationData {
        val transition = updateTransition(state, label = "errorMessageAnimation",)

        val alpha = transition.animateFloat(label = "errorMessageAlphaAnimation") { state ->
            when (state) {
                ErrorMessageState.HIDE -> 0f
                ErrorMessageState.SHOW -> 1f
            }
        }

        return remember(transition) { ErrorMessageAnimationData(alpha) }
    }

}

data class ErrorMessageAnimationData (val alpha : State<Float>)

enum class ErrorMessageState {
    HIDE,
    SHOW
}