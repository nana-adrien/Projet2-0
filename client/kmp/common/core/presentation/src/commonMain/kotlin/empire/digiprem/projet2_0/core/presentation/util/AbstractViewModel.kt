package empire.digiprem.projet2_0.core.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.ResultError
import kotlinx.coroutines.launch

abstract class AbstractViewModel : ViewModel() {

    protected fun <T, E : ResultError> launchOperation(
        operation: suspend () -> Result<T, E>,
        onSuccess: suspend (T) -> Unit,
        onFailure: suspend (E) -> Unit,
        onLoading: (suspend () -> Unit)? = null,
        onFinally: (suspend () -> Unit)? = null,
    ) {
        viewModelScope.launch {
            onLoading?.invoke()
            try {
                when (val result = operation()) {
                    is Result.Success -> onSuccess(result.data)
                    is Result.Failure -> onFailure(result.error)
                }
            } finally {
                onFinally?.invoke()
            }
        }
    }
}
