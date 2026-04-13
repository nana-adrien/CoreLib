package empire.digiprem.corelib.design_system.components.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import empire.digiprem.digi_task.core.design_system.components.menu.NestedMenuItemIdId

/**
 * Represents a unique identifier for a menu item.
 *
 * This interface is used to strongly type menu interactions.
 * It allows you to:
 * - Handle clicks using `when` expressions
 * - Avoid string-based navigation
 * - Structure IDs using sealed hierarchies (recommended)
 *
 * Example:
 * sealed interface AppMenuItemId : NestedMenuItemId {
 *     data object Home : AppMenuItemId
 *     data object Settings : AppMenuItemId
 * }
 */
interface NestedMenuItemId

/**
 * Represents a single item in the menu tree.
 *
 * @param id Unique identifier of the item (optional for non-clickable parents)
 * @param title Text displayed in the menu
 * @param icon Optional leading icon
 * @param enabled Whether the item is interactive
 * @param children Submenu items (for nested structure)
 *
 * 📌 Notes:
 * - If [children] is not empty → item acts as a parent (opens submenu)
 * - If [children] is empty → item is a "leaf" and can trigger selection
 * - If [id] is null → item is considered non-selectable (navigation only)
 */
data class NestedMenuItem(
    val id: NestedMenuItemId?,
    val title: String,
    val icon: ImageVector? = null,
    val enabled: Boolean = true,
    val children: List<NestedMenuItem> = emptyList()
)

/**
 * Defines all visual states for a menu item.
 *
 * Supports:
 * - Default state
 * - Selected state
 * - Disabled state
 *
 * This class acts as a bridge between your design system and Material3.
 *
 * @param textColor Default text color
 * @param leadingIconColor Default leading icon color
 * @param trailingIconColor Default trailing icon color
 *
 * @param selectedTextColor Text color when item is selected
 * @param selectedLeadingIconColor Leading icon color when selected
 * @param selectedTrailingIconColor Trailing icon color when selected
 *
 * @param disabledTextColor Text color when disabled
 * @param disabledLeadingIconColor Leading icon color when disabled
 * @param disabledTrailingIconColor Trailing icon color when disabled
 */
data class NestedMenuItemColors(
    val textColor: Color,
    val leadingIconColor: Color,
    val trailingIconColor: Color,

    val selectedTextColor: Color,
    val selectedLeadingIconColor: Color,
    val selectedTrailingIconColor: Color,

    val disabledTextColor: Color,
    val disabledLeadingIconColor: Color,
    val disabledTrailingIconColor: Color
) {
    /**
     * Factory for creating default menu item colors based on MaterialTheme.
     *
     * Must be called inside a @Composable context.
     */
    companion object {
        @Composable
        fun itemColors(
            textColor: Color = MaterialTheme.colorScheme.onSurface,
            leadingIconColor: Color = MaterialTheme.colorScheme.onSurface,
            trailingIconColor: Color = MaterialTheme.colorScheme.onSurface,

            selectedTextColor: Color = MaterialTheme.colorScheme.primary,
            selectedLeadingIconColor: Color = MaterialTheme.colorScheme.primary,
            selectedTrailingIconColor: Color = MaterialTheme.colorScheme.primary,

            disabledTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledLeadingIconColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledTrailingIconColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        ): NestedMenuItemColors {
            return NestedMenuItemColors(
                textColor = textColor,
                leadingIconColor = leadingIconColor,
                trailingIconColor = trailingIconColor,

                selectedTextColor = selectedTextColor,
                selectedLeadingIconColor = selectedLeadingIconColor,
                selectedTrailingIconColor = selectedTrailingIconColor,

                disabledTextColor = disabledTextColor,
                disabledLeadingIconColor = disabledLeadingIconColor,
                disabledTrailingIconColor = disabledTrailingIconColor
            )
        }
    }
}

/**
 * Defines the theming system for menu items.
 *
 * @param default Default colors applied to all items
 * @param overrides Optional color overrides per item ID
 *
 * 📌 Behavior:
 * - If an item ID exists in [overrides], its colors are used
 * - Otherwise, [default] colors are applied
 *
 * This allows:
 * - Global theming
 * - Targeted customization per item
 */
data class NestedMenuItemTheme(
    val default: NestedMenuItemColors,
    val overrides: Map<NestedMenuItemId, NestedMenuItemColors> = emptyMap()
) {
    companion object {
        /**
         * Creates a default theme using MaterialTheme colors.
         */
        @Composable
        fun defaultTheme() = NestedMenuItemTheme(
            default = NestedMenuItemColors.itemColors(),
        )
    }
}

/**
 * Converts custom menu colors into Material3 MenuItemColors.
 *
 * This is the bridge between:
 * - Your design system (NestedMenuItemColors)
 * - Material3 components (DropdownMenuItem)
 *
 * @param isSelected Whether the item is currently selected
 */
@Composable
private fun NestedMenuItemColors.toMaterialColors(
    isSelected: Boolean
): MenuItemColors {
    return MenuItemColors(
        textColor = if (isSelected) selectedTextColor else textColor,
        leadingIconColor = if (isSelected) selectedLeadingIconColor else leadingIconColor,
        trailingIconColor = if (isSelected) selectedTrailingIconColor else trailingIconColor,
        disabledTextColor = disabledTextColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
    )
}

/**
 * Resolves the correct color configuration for a given item ID.
 *
 * @param id Menu item identifier
 * @return Matching colors or default if not found
 *
 * 📌 Matching strategy:
 * - Matches by class type (useful for sealed hierarchies)
 * - Falls back to default if no override exists
 */
fun NestedMenuItemTheme.colorsFor(id: NestedMenuItemId?): NestedMenuItemColors {
    val resolved = id?.let { currentId ->
        overrides.entries.firstOrNull {
            it.key::class == currentId::class
        }?.value
    }
    return resolved ?: default
}

/**
 * NestedMenu
 *
 * A fully reusable, hierarchical dropdown menu component built with Jetpack Compose.
 *
 * Designed for complex navigation structures, this component supports:
 *
 * ✅ Infinite nested menus (tree structure)
 * ✅ Strongly typed item selection (no strings)
 * ✅ Custom per-item theming
 * ✅ Material3 integration
 * ✅ Recursive rendering
 *
 * ------------------------------------------------------------
 * 📌 CORE CONCEPT
 * ------------------------------------------------------------
 *
 * The menu is built as a tree of [NestedMenuItem].
 *
 * Each item can:
 * - Be selectable (leaf node)
 * - Open a submenu (parent node)
 *
 * Example structure:
 *```
 * Messages
 *   ├── Inbox
 *   ├── Sent
 *   └── Archive
 *         ├── 2024
 *         └── 2023
 *```
 * ------------------------------------------------------------
 * 📌 SELECTION MODEL
 * ------------------------------------------------------------
 *
 * - Only items WITHOUT children trigger selection
 * - Selection is strongly typed via [NestedMenuItemId]
 * - Ideal for use with `when` expressions
 *
 * ------------------------------------------------------------
 * 📌 THEME SYSTEM
 * ------------------------------------------------------------
 *
 * The component uses [NestedMenuItemTheme]:
 *
 * - `default` → applied globally
 * - `overrides` → applied per item ID
 *
 * Example:
 *```
 * itemColors = NestedMenuItemTheme.defaultTheme().copy(
 *     overrides = mapOf(
 *         AppMenuItemId.Messages.Archive.Y2024 to customColors
 *     )
 * )
 *```
 * ------------------------------------------------------------
 * 📌 PARAMETERS
 * ------------------------------------------------------------
 *
 * @param expanded Controls visibility of the menu
 * @param items Root menu items
 * @param modifier Layout modifier
 * @param onDismissRequest Called when menu is dismissed
 * @param offset Position offset
 * @param scrollState Scroll behavior
 * @param properties Popup configuration
 * @param shape Menu shape
 * @param containerColor Background color
 * @param tonalElevation Elevation for tonal surfaces
 * @param shadowElevation Shadow elevation
 * @param border Optional border
 * @param itemColors Theming configuration
 * @param onSelectedItem Callback when a leaf item is selected
 *
 * ------------------------------------------------------------
 * 📌 BEHAVIOR
 * ------------------------------------------------------------
 *
 * - Click on parent → opens submenu
 * - Click on leaf → triggers selection + closes menu
 * - Submenus are rendered recursively
 *
 * ------------------------------------------------------------
 * 📌 EXAMPLE USAGE
 * ------------------------------------------------------------
 *```
 *    NestedMenu(
 *        expanded = true,
 *     items = menuItems,
 *    onDismissRequest = { /* close */ },
 *
 *     itemColors = NestedMenuItemTheme.defaultTheme().copy(
 *         overrides = mapOf(
 *             AppMenuItemId.Messages.Archive.Y2024 to
 *                 NestedMenuItemColors.itemColors(
 *                    textColor = MaterialTheme.colorScheme.error
 *                )
 *       )
 *    )
 *
 *   ) { id ->
 *
 *     when (id as AppMenuItemId) {
 *        AppMenuItemId.Home -> {}
 *         AppMenuItemId.Messages.Inbox -> {}
 *         AppMenuItemId.Messages.Archive.Y2024 -> {}
 *        AppMenuItemId.Settings.Security -> {}
 *     }
 *
 *```
 *
 *
 * ------------------------------------------------------------
 * 📌 NOTES
 * ------------------------------------------------------------
 *
 * - Parent items are NOT selectable (unless you give them an ID and no children)
 * - IDs should preferably be implemented using sealed interfaces
 * - Color overrides match by type (class), not instance
 * - Fully compatible with Kotlin Multiplatform
 */
@Composable
fun NestedMenu(
    expanded: Boolean = true,
    items: List<NestedMenuItem>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    scrollState: ScrollState = rememberScrollState(),
    properties: PopupProperties = PopupProperties(focusable = true),
    shape: Shape = MenuDefaults.shape,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = MenuDefaults.TonalElevation,
    shadowElevation: Dp = MenuDefaults.ShadowElevation,
    border: BorderStroke? = null,
    itemColors: NestedMenuItemTheme = NestedMenuItemTheme.defaultTheme(),
    onSelectedItem: (NestedMenuItemId) -> Unit
) {
    var selectedMenu by remember { mutableStateOf<NestedMenuItem?>(null) }
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        shadowElevation = shadowElevation,
        offset = offset,
        shape = shape,
        border = border,
        properties = properties,
        scrollState = scrollState,
        tonalElevation = tonalElevation,
        containerColor = containerColor,
        onDismissRequest = onDismissRequest
    ) {
        items.forEach { item ->
            var expandedSubMenu by rememberSaveable { mutableStateOf(false) }
            val isSelected = selectedMenu == item
            DropdownMenuItem(
                colors = itemColors.colorsFor(item.id).toMaterialColors(isSelected),
                text = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                leadingIcon = {
                    item.icon?.let {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                        )
                    }
                },
                trailingIcon = {
                    if (item.children.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                        )
                    }
                    NestedMenu(
                        expanded = expandedSubMenu,
                        modifier = modifier,
                        items = item.children,
                        itemColors = itemColors,
                        shadowElevation = shadowElevation,
                        offset = offset,
                        shape = shape,
                        border = border,
                        properties = properties,
                        scrollState = scrollState,
                        tonalElevation = tonalElevation,
                        containerColor = containerColor,
                        onDismissRequest = {
                            expandedSubMenu = false
                            selectedMenu = null
                        },
                        onSelectedItem = {
                            expandedSubMenu = false
                            onSelectedItem(it)
                            onDismissRequest.invoke()
                        }
                    )
                },
                onClick = {
                    selectedMenu = item
                    if (item.children.isNotEmpty()) {
                        expandedSubMenu = true
                    } else {
                        item.id?.let { onSelectedItem(it) }
                        onDismissRequest.invoke()
                        selectedMenu = null
                    }
                }
            )
        }
    }
}


@Composable
private fun DigiTaskMenuPreview() {
    NestedMenu(
        onDismissRequest = {},
        items = listOf()
    ) {
        when (it) {
            NestedMenuItemIdId.Help -> TODO()
            NestedMenuItemIdId.Home -> TODO()
            NestedMenuItemIdId.Messages.Archive.Y2023 -> TODO()
            NestedMenuItemIdId.Messages.Archive.Y2024 -> {
                println("bonJour le monde $it")
            }

            NestedMenuItemIdId.Messages.Inbox -> TODO()
            NestedMenuItemIdId.Messages.Sent -> TODO()
            NestedMenuItemIdId.Profile.Edit -> TODO()
            NestedMenuItemIdId.Profile.View -> TODO()
            NestedMenuItemIdId.Settings.Notifications -> TODO()
            NestedMenuItemIdId.Settings.Security -> TODO()
        }
    }
}


@Composable
private fun DigiTaskMenuTheme(darkTheme: Boolean = false) {
    MaterialTheme(
        //darkTheme = darkTheme
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            DigiTaskMenuPreview()
        }
    }
}


// Light
@Composable
private fun DigiTaskMenuLightThemePreview() {
    DigiTaskMenuTheme(
        darkTheme = false
    )
}

// Dark
@Composable
private fun DigiTaskMenuDarkThemePreview() {
    DigiTaskMenuTheme(
        darkTheme = true
    )
}

// Phone


@Preview(
    name = "Phone",
    group = "DigiTaskMenu Light Theme",
    device = Devices.PIXEL_3A,
)
@Composable
private fun DigiTaskMenuLightThemePhonePreview() {
    DigiTaskMenuLightThemePreview()
}


// Phone

@Preview(
    name = "Phone",
    group = "DigiTaskMenu Dark Theme",
    device = Devices.PIXEL_3A,
)
@Composable
private fun DigiTaskMenuDarkThemePhonePreview() {
    DigiTaskMenuDarkThemePreview()
}

// Table
@Preview(
    name = "Table",
    group = "DigiTaskMenu Light Theme",
    device = Devices.TABLET,
)

@Composable
private fun DigiTaskMenuLightThemeTablePreview() {
    DigiTaskMenuLightThemePreview()
}

// Table

@Preview(
    name = "Table",
    group = "DigiTaskMenu Dark Theme",
    device = Devices.TABLET,
)
@Composable
private fun DigiTaskMenuDarkThemeTablePreview() {
    DigiTaskMenuDarkThemePreview()
}

// Desktop
@Preview(
    name = "Desktop",
    group = "DigiTaskMenu Light Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun DigiTaskMenuLightThemeDesktopPreview() {
    DigiTaskMenuLightThemePreview()
}

// Desktop

@Preview(
    name = "Desktop",
    group = "DigiTaskMenu Dark Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun DigiTaskMenuDarkThemeDesktopPreview() {
    DigiTaskMenuDarkThemePreview()
}




