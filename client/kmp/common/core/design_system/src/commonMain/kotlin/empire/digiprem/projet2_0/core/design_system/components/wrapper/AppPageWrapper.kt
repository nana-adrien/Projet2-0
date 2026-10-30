package empire.digiprem.projet2_0.core.design_system.components.wrapper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import empire.digiprem.projet2_0.core.design_system.components.dialog.PDZErrorDialog
import empire.digiprem.projet2_0.core.design_system.components.states.PDZErrorLevel
import empire.digiprem.projet2_0.core.design_system.components.states.PDZLoadingSpinner
import empire.digiprem.projet2_0.core.design_system.components.states.PDZPageState

@Composable
fun PDZPageWrapper(
    state: PDZPageState,
    modifier: Modifier = Modifier,
    errorPage: @Composable ((PDZErrorLevel.Page) -> Unit)? = null,
    errorDialog: @Composable ((PDZErrorLevel.Dialog) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> PDZLoadingSpinner()

            state.error is PDZErrorLevel.Page && errorPage != null ->
                errorPage(state.error as PDZErrorLevel.Page)

            else -> {
                content()
                val dialogError = state.error as? PDZErrorLevel.Dialog
                if (dialogError != null) {
                    if (errorDialog != null) {
                        errorDialog(dialogError)
                    } else {
                        PDZErrorDialog(
                            message   = dialogError.message,
                            onDismiss = { state.clearError() },
                        )
                    }
                }
            }
        }
    }
}
