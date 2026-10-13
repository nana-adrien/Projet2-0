package empire.digiprem.projet2_0.core.design_system.components.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

sealed class PDZErrorLevel {
    data class Page(val message: String) : PDZErrorLevel()
    data class Dialog(val message: String) : PDZErrorLevel()
}

@Stable
class PDZPageState {
    private val _isLoading = mutableStateOf(false)
    private val _error = mutableStateOf<PDZErrorLevel?>(null)

    val isLoading: Boolean get() = _isLoading.value
    val error: PDZErrorLevel? get() = _error.value

    fun setLoading(loading: Boolean) { _isLoading.value = loading }
    fun setError(level: PDZErrorLevel) { _error.value = level; _isLoading.value = false }
    fun clearError() { _error.value = null }
    fun setReady() { _isLoading.value = false; _error.value = null }
}

@Composable
fun rememberPDZPageState(): PDZPageState = remember { PDZPageState() }
