package empire.digiprem.corelib

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import empire.digiprem.corelib.design_system.layout.DialogSheetAdaptiveLayout
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
            DialogSheetAdaptiveLayout(
                visible = true,
                onDismiss = {

                }
            ){
                Box(
                    modifier = Modifier
                        .height(300.dp).fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom =16.dp),
                    contentAlignment = Alignment.Center
                ) {

                }
            }
        }
    }
}