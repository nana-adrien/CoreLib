package empire.digiprem.corelib.presentation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager


/**
 * clearFocusOnTap is a custom Modifier extension in Jetpack Compose
 * that automatically clears the focus from a component (e.g., a TextField)
 * when the user taps anywhere on the screen.
 *
 * It is very useful for:
 *
 * - Hiding the keyboard
 * - Deselecting input fields
 * - Improving the overall user experience on mobile
 */

@Composable
fun Modifier.clearFocusOnTap(): Modifier {
    currentDeviceConfigure()
    val focusManager= LocalFocusManager.current
    return this.pointerInput(Unit){
        detectTapGestures {
            focusManager.clearFocus()
        }
    }

}