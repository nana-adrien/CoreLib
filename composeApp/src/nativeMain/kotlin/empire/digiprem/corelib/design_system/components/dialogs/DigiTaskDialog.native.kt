package empire.digiprem.corelib.design_system.components.dialogs

import androidx.compose.ui.window.Dialog

@androidx.compose.runtime.Composable
internal actual fun PlatformDigiTaskDialog(
    onDismissRequest: () -> Unit,
    properties: androidx.compose.ui.window.DialogProperties,
    content: @androidx.compose.runtime.Composable (() -> Unit)
) {
    Dialog(
        onDismissRequest=onDismissRequest,
        properties=properties,
        content=content
    )
}