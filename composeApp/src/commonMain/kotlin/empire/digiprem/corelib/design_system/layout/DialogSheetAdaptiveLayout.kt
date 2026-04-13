package empire.digiprem.corelib.design_system.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.unit.dp
import empire.digiprem.corelib.presentation.currentDeviceConfigure
import empire.digiprem.corelib.design_system.components.bottom_sheet.DigiTaskBottomSheet
import empire.digiprem.corelib.design_system.components.dialogs.DigiTaskDialog



@OptIn(/**
 * A responsive modal component that automatically adapts its presentation
 * based on the current device type (mobile, tablet, desktop).
 *
 * ## Behavior
 * - On mobile devices: displays a [DigiTaskBottomSheet]
 * - On tablet/desktop: displays a [DigiTaskDialog]
 *
 * ## Features
 * - Automatic adaptive layout (dialog or bottom sheet)
 * - Control over outside click dismissal
 * - Optional expanded mode for mobile bottom sheet
 * - Theming support (shape and container color)
 * - Unified API for cross-platform usage
 *
 * ## Parameters
 *
 * @param visible
 * Controls whether the component is displayed or hidden.
 *
 * @param dismissOnOutsideClick
 * If true, allows the user to dismiss the modal by clicking outside.
 * If false, outside clicks are ignored.
 *
 * @param enabledMobileExpendedContent
 * Enables expanded state support for bottom sheet on mobile devices.
 *
 * @param modifier
 * Optional [Modifier] applied to the root container.
 *
 * @param dialogShape
 * Shape used for the dialog on desktop/tablet devices.
 *
 * @param bottomSheetShape
 * Shape used for the bottom sheet on mobile devices.
 *
 * @param containerColor
 * Background color applied to both dialog and bottom sheet.
 *
 * @param onDismiss
 * Callback invoked when the component is dismissed by the user.
 *
 * @param content
 * Composable content displayed inside the modal container.
 *
 * ## Usage
 *
 * ```kotlin
 * DialogSheetAdaptiveLayout(
 *     visible = true,
 *     dismissOnOutsideClick = true,
 *     onDismiss = { /* handle close */ }
 * ) {
 *     Text("Hello world")
 * }
 * ```
 *
 * ## Notes
 * This component is part of the DigiPrem Design System and ensures
 * consistent modal behavior across platforms.
 */ExperimentalMaterial3Api::class)
@Composable
fun DialogSheetAdaptiveLayout(
    visible: Boolean = false,
    dismissOnOutsideClick: Boolean = true,
    enabledMobileExpendedContent: Boolean = false,
    modifier: Modifier = Modifier,
    dialogShape: Shape = RoundedCornerShape(size = 28.dp),
    bottomSheetShape: Shape = BottomSheetDefaults.ExpandedShape,
    contentWindowInsets: @Composable (() -> WindowInsets) ={ BottomSheetDefaults.windowInsets},
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onDismiss: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val currentConfiguration = currentDeviceConfigure()
    LaunchedEffect(visible) {
        if (visible) {
            showBottomSheet = true
        }
    }
    if (currentConfiguration.isMobile) {
        if (showBottomSheet) {
            DigiTaskBottomSheet(
                visibility = visible,
                shape = bottomSheetShape,
                dismissOnOutsideClick = dismissOnOutsideClick,
                modifier = modifier,
                containerColor=containerColor,
                contentWindowInsets=contentWindowInsets,
                enabledExpendedContent = enabledMobileExpendedContent,
                onDismiss = {
                    showBottomSheet = false
                    if (visible) {
                        onDismiss()
                    }
                },
                content = content
            )
        }

    } else {
        if (visible) {
            DigiTaskDialog(
                shape = dialogShape,
                dismissOnOutsideClick = dismissOnOutsideClick,
                containerColor=containerColor,
                modifier = modifier,
                onDismiss = onDismiss,
                content = { content() }
            )
        }
    }
}


@Composable
private fun DialogSheetAdaptiveLayoutPreview(
    visibility: Boolean = true,
    canDismissibleChange: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    DialogSheetAdaptiveLayout(
        dismissOnOutsideClick = true,
        visible = visibility,
        modifier = Modifier,
        enabledMobileExpendedContent = true,
        onDismiss = onDismiss,
    ) {
        Box(
            modifier = Modifier
                .height(300.dp).fillMaxWidth()
                .padding(horizontal =16.dp)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}


@Composable
private fun DialogSheetAdaptiveLayoutTheme(darkTheme: Boolean = false) {

    var sheetContent by remember { mutableStateOf(true) }
    MaterialTheme(
       // darkTheme = darkTheme
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
        }
        if (sheetContent) {
            DialogSheetAdaptiveLayoutPreview {
                sheetContent = false
            }
        }
    }
}


// Light
@Composable
private fun DialogSheetAdaptiveLayoutLightThemePreview() {
    DialogSheetAdaptiveLayoutTheme(
        darkTheme = false
    )
}

// Dark
@Composable
private fun DialogSheetAdaptiveLayoutDarkThemePreview() {
    DialogSheetAdaptiveLayoutTheme(
        darkTheme = true
    )
}

// Phone


@Preview(
    name = "Phone",
    group = "DialogSheetAdaptiveLayout Light Theme",
    device = Devices.PIXEL_3A,
)
@Composable
private fun DialogSheetAdaptiveLayoutLightThemePhonePreview() {
    DialogSheetAdaptiveLayoutLightThemePreview()
}


// Phone

@Preview(
    name = "Phone",
    group = "DialogSheetAdaptiveLayout Dark Theme",
    device = Devices.PIXEL_3A,
)
@Composable
private fun DialogSheetAdaptiveLayoutDarkThemePhonePreview() {
    DialogSheetAdaptiveLayoutDarkThemePreview()
}

// Table
@Preview(
    name = "Table",
    group = "DialogSheetAdaptiveLayout Light Theme",
    device = Devices.TABLET,
)

@Composable
private fun DialogSheetAdaptiveLayoutLightThemeTablePreview() {
    DialogSheetAdaptiveLayoutLightThemePreview()
}

// Table

@Preview(
    name = "Table",
    group = "DialogSheetAdaptiveLayout Dark Theme",
    device = Devices.TABLET,
)
@Composable
private fun DialogSheetAdaptiveLayoutDarkThemeTablePreview() {
    DialogSheetAdaptiveLayoutDarkThemePreview()
}

// Desktop
@Preview(
    name = "Desktop",
    group = "DialogSheetAdaptiveLayout Light Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun DialogSheetAdaptiveLayoutLightThemeDesktopPreview() {
    DialogSheetAdaptiveLayoutLightThemePreview()
}

// Desktop

@Preview(
    name = "Desktop",
    group = "DialogSheetAdaptiveLayout Dark Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun DialogSheetAdaptiveLayoutDarkThemeDesktopPreview() {
    DialogSheetAdaptiveLayoutDarkThemePreview()
}




