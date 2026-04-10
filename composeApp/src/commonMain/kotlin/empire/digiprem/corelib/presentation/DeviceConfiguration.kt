package empire.digiprem.corelib.presentation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowWidthSizeClass
import kotlin.math.min


enum class DeviceConfiguration{
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;


    val isMobile: Boolean
        get() = this in  listOf(MOBILE_PORTRAIT,MOBILE_LANDSCAPE)

    val isWideScreen: Boolean
        get()=this in listOf(TABLET_LANDSCAPE, DESKTOP)


    companion object{

        fun fromWindowsSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration{

                val width = windowSizeClass.windowWidthSizeClass
                val isLandscape = windowSizeClass.minWidthDp > windowSizeClass.minHeightDp

                return when (width) {
                    WindowWidthSizeClass.COMPACT -> {
                        if (isLandscape) DeviceConfiguration.MOBILE_LANDSCAPE
                        else DeviceConfiguration.MOBILE_PORTRAIT
                    }

                    WindowWidthSizeClass.MEDIUM -> {
                        if (isLandscape) DeviceConfiguration.TABLET_LANDSCAPE
                        else DeviceConfiguration.TABLET_PORTRAIT
                    }

                    WindowWidthSizeClass.EXPANDED -> {
                        DeviceConfiguration.DESKTOP
                    }

                    else -> DeviceConfiguration.MOBILE_PORTRAIT
                }

           /*
            return with(windowSizeClass){
               // val smallestSideDp= min(minWidthDp, minHeightDp)
              //  val isTablet=smallestSideDp>= WIDTH_DP_MEDIUM_LOWER_BOUND
                val isTablet = isWidthAtLeastBreakpoint(Medium)
                val isDesktop = isWidthAtLeastBreakpoint(WindowSizeClassBreakpoint.Expanded)

                val isLandscape = minWidthDp > minHeightDp

                when{
                    !isTablet && !isLandscape-> DeviceConfiguration.MOBILE_PORTRAIT
                    !isTablet && isLandscape -> DeviceConfiguration.MOBILE_LANDSCAPE
                    isTablet && !isLandscape-> DeviceConfiguration.TABLET_PORTRAIT
                    isTablet && isLandscape -> DeviceConfiguration.TABLET_LANDSCAPE
                    else -> DeviceConfiguration.DESKTOP
                }
            }*/
        }
    }

}

/**
 * DeviceConfiguration represents the different types of device layouts
 * based on screen size and orientation.
 *
 * It helps adapt the UI for:
 * - Mobile devices (portrait / landscape)
 * - Tablets (portrait / landscape)
 * - Desktop environments
 *
 * Features:
 *
 * - isMobile:
 *   Returns true if the device is a mobile (portrait or landscape).
 *
 * - isWideScreen:
 *   Returns true for wide layouts such as tablet landscape and desktop.
 *
 * Companion Object:
 *
 * - fromWindowsSizeClass(windowSizeClass):
 *   Maps a WindowSizeClass to a DeviceConfiguration.
 *
 *   Logic:
 *   - Determines the smallest screen dimension (dp)
 *   - Detects if the device is a tablet using a threshold
 *   - Detects orientation (landscape or portrait)
 *   - Returns the corresponding configuration
 *
 *   This allows consistent responsive UI behavior across devices.
 *
 *
 * currentDeviceConfigure():
 *
 * Composable helper function that provides the current DeviceConfiguration
 * based on the window size using currentWindowAdaptiveInfo().
 *
 * Usage:
 * - Call inside a @Composable to adapt UI depending on device type
 *
 * Example:
 * ```
 * val config = currentDeviceConfigure()
 * if (config.isMobile) {
 *     // Mobile UI
 * } else if (config.isWideScreen) {
 *     // Tablet/Desktop UI
 * }
 * ```
 */
@Composable
fun currentDeviceConfigure():DeviceConfiguration{
    val windowSizeClass= currentWindowAdaptiveInfo().windowSizeClass
    return DeviceConfiguration.fromWindowsSizeClass(windowSizeClass)
}