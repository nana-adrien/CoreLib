package empire.digiprem.corelib.design_system.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DigiTaskDialog(
    dismissOnOutsideClick: Boolean = true,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(size = 28.dp),
    properties: DialogProperties = DialogProperties(),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismiss){
        
    }
    PlatformDigiTaskDialog(
        properties=properties,
        onDismissRequest = {
            if (dismissOnOutsideClick) {
                onDismiss()
            }
        },
    ) {
        Column(
            modifier = Modifier.heightIn(max = 500.dp).widthIn(max = 600.dp)
                .then(modifier)
                .shadow(elevation = 40.dp,shape = shape)
                .background(color =containerColor, shape = shape)
            ,
            content = content
        )
    }
}

@Composable
internal expect fun PlatformDigiTaskDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable (() -> Unit)
)

@Composable
private fun DigiTaskDialogPreview() {
    DigiTaskDialog(
        onDismiss = {

        },
        // modifier = Modifier
    ) {
        Column(
            modifier = Modifier.width(400.dp)
                .height(250.dp),
        ) {
            Text("Digi Task Dialog Preview")

        }
    }
}


@Composable
private fun DigiTaskDialogTheme(darkTheme: Boolean = false) {
    MaterialTheme(
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            DigiTaskDialogPreview()
        }
    }
}


// Light
@Composable
private fun DigiTaskDialogLightThemePreview() {
    DigiTaskDialogTheme(
        darkTheme = false
    )
}

// Dark
@Composable
private fun DigiTaskDialogDarkThemePreview() {
    DigiTaskDialogTheme(
        darkTheme = true
    )
}

// Phone


@Preview(
    name = "Phone",
    group = "DigiTaskDialog Light Theme",
    device = Devices.PIXEL_3A,
)
@Composable
private fun DigiTaskDialogLightThemePhonePreview() {
    DigiTaskDialogLightThemePreview()
}


// Phone

@Preview(
    name = "Phone",
    group = "DigiTaskDialog Dark Theme",
    device = Devices.PIXEL_3A,
)
@Composable
private fun DigiTaskDialogDarkThemePhonePreview() {
    DigiTaskDialogDarkThemePreview()
}

// Table
@Preview(
    name = "Table",
    group = "DigiTaskDialog Light Theme",
    device = Devices.TABLET,
)

@Composable
private fun DigiTaskDialogLightThemeTablePreview() {
    DigiTaskDialogLightThemePreview()
}

// Table

@Preview(
    name = "Table",
    group = "DigiTaskDialog Dark Theme",
    device = Devices.TABLET,
)
@Composable
private fun DigiTaskDialogDarkThemeTablePreview() {
    DigiTaskDialogDarkThemePreview()
}

// Desktop
@Preview(
    name = "Desktop",
    group = "DigiTaskDialog Light Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun DigiTaskDialogLightThemeDesktopPreview() {
    DigiTaskDialogLightThemePreview()
}

// Desktop

@Preview(
    name = "Desktop",
    group = "DigiTaskDialog Dark Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun DigiTaskDialogDarkThemeDesktopPreview() {
    DigiTaskDialogDarkThemePreview()
}




