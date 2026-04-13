package empire.digiprem.digi_task.core.design_system.components.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import empire.digiprem.corelib.design_system.components.menu.NestedMenuItem
import empire.digiprem.corelib.design_system.components.menu.NestedMenuItemId


sealed interface NestedMenuItemIdId: NestedMenuItemId {
    // Root
    data object Home : NestedMenuItemIdId
    data object Help : NestedMenuItemIdId

    // Profil
    sealed interface Profile : NestedMenuItemIdId {
        data object View : Profile
        data object Edit : Profile
    }

    // Messages
    sealed interface Messages : NestedMenuItemIdId {
        data object Inbox : Messages
        data object Sent : Messages

        sealed interface Archive : Messages {
            data object Y2024 : Archive
            data object Y2023 : Archive
        }
    }

    // Settings
    sealed interface Settings : NestedMenuItemIdId {
        data object Security : Settings
        data object Notifications : Settings
    }

}


val testMenu = listOf(
    NestedMenuItem(
        id = NestedMenuItemIdId.Home,
        title = "Accueil",
        icon = Icons.Default.Home,
    ),

    NestedMenuItem(
        id = null,
        title = "Profil",
        icon = Icons.Default.Person,
        children = listOf(
            NestedMenuItem(
                id = NestedMenuItemIdId.Profile.View,
                title = "Voir profil",
                icon = Icons.Default.Visibility
            ),
            NestedMenuItem(
                id = NestedMenuItemIdId.Profile.Edit,
                title = "Modifier profil",
                icon = Icons.Default.Edit
            )
        )
    ),

    NestedMenuItem(
        id = null,
        title = "Messages",
        icon = Icons.Default.Email,
        children = listOf(
            NestedMenuItem(
                id = NestedMenuItemIdId.Messages.Inbox,
                title = "Boîte de réception",
                icon = Icons.Default.Inbox
            ),
            NestedMenuItem(
                id = NestedMenuItemIdId.Messages.Sent,
                title = "Envoyés",
                icon = Icons.Default.Send
            ),
            NestedMenuItem(
                id = null,
                title = "Archivés",
                icon = Icons.Default.Archive,
                children = listOf(
                    NestedMenuItem(
                        id = NestedMenuItemIdId.Messages.Archive.Y2024,
                        title = "2024",
                        icon = Icons.Default.DateRange
                    ),
                    NestedMenuItem(
                        id = NestedMenuItemIdId.Messages.Archive.Y2023,
                        title = "2023",
                        icon = Icons.Default.DateRange
                    )
                )
            )
        )
    ),
    NestedMenuItem(
        id = null,
        title = "Settings",
        icon = Icons.Default.Settings,
        children = listOf(
            NestedMenuItem(
                id = NestedMenuItemIdId.Settings.Security,
                title = "Security",
                icon = Icons.Default.Security
            ),
            NestedMenuItem(
                id = NestedMenuItemIdId.Settings.Notifications,
                title = "Notifications",
                icon = Icons.Default.Notifications
            ),

            )
    )
)