package empire.digiprem.corelib.design_system.components.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Devices


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigiTaskBottomSheet(
    visibility: Boolean = false,
    dismissOnOutsideClick: Boolean = true,
    modifier: Modifier = Modifier,
    shape: Shape =  BottomSheetDefaults.ExpandedShape,
    containerColor: Color= MaterialTheme.colorScheme.surface,
    enabledExpendedContent: Boolean = false,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val hidden by rememberUpdatedState(!visibility)
    val currentDismissOnOutside by rememberUpdatedState(dismissOnOutsideClick)
    val currentExpandedEnabled by rememberUpdatedState(enabledExpendedContent)

    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { newValue ->
            when (newValue) {
                SheetValue.Hidden -> hidden || currentDismissOnOutside
                SheetValue.Expanded -> currentExpandedEnabled
                else -> true
            }
        }
    )
    LaunchedEffect(visibility) {
        if (visibility) {
            sheetState.show()
        } else {
            sheetState.hide()
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        shape=shape,
        containerColor = containerColor,
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = { WindowInsets.navigationBars },
        content =content
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigiTaskBottomSheetPreview() {

    val scope = rememberCoroutineScope()
    var sheetContent by remember { mutableStateOf(true) }
    if (sheetContent) {
        DigiTaskBottomSheet(
            modifier = Modifier.fillMaxSize(),
            enabledExpendedContent = true,
            onDismiss = {
                sheetContent = false
            },
        ) {
            Box(
                Modifier.fillMaxHeight().fillMaxWidth().background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.Center
            ) {

            }
        }
    }

}


@Composable
private fun DigiTaskBottomSheetTheme(darkTheme: Boolean = false) {
    MaterialTheme(
        //darkTheme = darkTheme
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.onBackground),
        ) {
        }
        DigiTaskBottomSheetPreview()
    }
}


// Light
@Composable
private fun DigiTaskBottomSheetLightThemePreview() {
    DigiTaskBottomSheetTheme(
        darkTheme = false
    )
}

// Dark
@Composable
private fun DigiTaskBottomSheetDarkThemePreview() {
    DigiTaskBottomSheetTheme(
        darkTheme = true
    )
}

// Phone


@Preview(
    name = "Phone",
    group = "DigiTaskBottomSheet Light Theme",
    // device = Devices.PIXEL_3A,
)
@Composable
private fun DigiTaskBottomSheetLightThemePhonePreview() {
    DigiTaskBottomSheetLightThemePreview()
}


// Phone

@Preview(
    name = "Phone",
    group = "DigiTaskBottomSheet Dark Theme",
    //device = Devices.PIXEL_3A,
)
@Composable
private fun DigiTaskBottomSheetDarkThemePhonePreview() {
    DigiTaskBottomSheetDarkThemePreview()
}

// Table
@Preview(
    name = "Table",
    group = "DigiTaskBottomSheet Light Theme",
    device = Devices.TABLET,
)

@Composable
private fun DigiTaskBottomSheetLightThemeTablePreview() {
    DigiTaskBottomSheetLightThemePreview()
}

// Table

@Preview(
    name = "Table",
    group = "DigiTaskBottomSheet Dark Theme",
    device = Devices.TABLET,
)
@Composable
private fun DigiTaskBottomSheetDarkThemeTablePreview() {
    DigiTaskBottomSheetDarkThemePreview()
}

// Desktop
@Preview(
    name = "Desktop",
    group = "DigiTaskBottomSheet Light Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun DigiTaskBottomSheetLightThemeDesktopPreview() {
    DigiTaskBottomSheetLightThemePreview()
}

// Desktop

@Preview(
    name = "Desktop",
    group = "DigiTaskBottomSheet Dark Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun DigiTaskBottomSheetDarkThemeDesktopPreview() {
    DigiTaskBottomSheetDarkThemePreview()
}




