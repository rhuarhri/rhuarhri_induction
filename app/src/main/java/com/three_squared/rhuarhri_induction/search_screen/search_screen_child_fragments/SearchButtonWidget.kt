package com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class SearchButtonWidget {

    //TODO App Presentation slide 7
    @Composable
    fun searchButton(state : SearchButtonState, onClick : () -> Unit) {

        val animationData = buttonAnimation(buttonState = state)

        Row(horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                    if (state == SearchButtonState.ENABLED) {
                        onClick.invoke()
                    }
                },
                Modifier
                    .alpha(animationData.alpha.value)
                    .height(animationData.height.value)
                    .width(animationData.width.value),
                colors = ButtonDefaults.buttonColors(backgroundColor = animationData.color.value)
            ) {
                buttonContent(buttonState = state)
            }
        }
    }

    @Composable
    fun buttonContent(buttonState : SearchButtonState) {
        if (buttonState == SearchButtonState.LOADING) {
            CircularProgressIndicator(Modifier.height(40.dp))
        } else if (buttonState == SearchButtonState.ENABLED) {
            Text("Search")
        } else {
            Text("Disabled")
        }
    }

    @Composable
    fun buttonAnimation(buttonState : SearchButtonState) : ButtonAnimationData {
        val transition = updateTransition(buttonState, label = "buttonAnimation",)
        val color = transition.animateColor(label = "buttonColorAnimation") { state ->
            when (state) {
                SearchButtonState.DISABLED -> Color.Gray
                SearchButtonState.ENABLED -> MaterialTheme.colors.primary
                SearchButtonState.LOADING -> Color.White
            }
        }

        val alpha = transition.animateFloat(label = "buttonAlphaAnimation") { state ->
            when (state) {
                SearchButtonState.DISABLED -> 0.2f
                SearchButtonState.ENABLED -> 1f
                SearchButtonState.LOADING -> 1f
            }
        }

        val width = transition.animateDp(label = "buttonWidthAnimation") { state ->
            when(state) {
                SearchButtonState.DISABLED -> 100.dp
                SearchButtonState.ENABLED -> 150.dp
                SearchButtonState.LOADING -> 100.dp
            }
        }

        val height = transition.animateDp(label = "buttonColorAnimation") { state ->
            when(state) {
                SearchButtonState.DISABLED -> 40.dp
                SearchButtonState.ENABLED -> 40.dp
                SearchButtonState.LOADING -> 60.dp
            }
        }

        return remember(transition) { ButtonAnimationData(color, alpha, height, width) }
    }

}

data class ButtonAnimationData(val color: State<Color>,
                               val alpha : State<Float>,
                               val height : State<Dp>,
                               val width : State<Dp>
)


enum class SearchButtonState {
    ENABLED,
    DISABLED,
    LOADING,
}