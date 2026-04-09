package empire.digiprem.corelib

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import empire.digiprem.corelib.util.asString
import empire.digiprem.shared.util.UiText


@Composable
@Preview
fun App() {
    MaterialTheme {

        val text= UiText.DynamicString("bonjour le mode ")
        Box(
            modifier= Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text=text.asString()
            )
        }
    }
}