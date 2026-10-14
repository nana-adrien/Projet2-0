package empire.digiprem.projet2_0

import androidx.compose.ui.window.ComposeUIViewController
import empire.digiprem.projet2_0.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }
}
