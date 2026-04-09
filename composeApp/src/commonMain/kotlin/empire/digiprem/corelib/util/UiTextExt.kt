package empire.digiprem.corelib.util

import androidx.compose.runtime.Composable
import empire.digiprem.shared.util.UiText
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

   @Composable
    fun UiText.asString():String{
       return when(this){
            is UiText.DynamicString ->value
            is UiText.Resource -> stringResource(
                id,
                *args
            )
        }
    }

