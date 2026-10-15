package empire.digiprem.projet2_0.feature.dashboard.classements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.projet2_0.core.domain.util.onSuccess
import empire.digiprem.projet2_0.feature.deuxzero.usecase.ListMesDeuxZeroUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Résout le 2-0 courant pour l'onglet Classements — pas de sélecteur multi-2-0 pour l'instant
 * (simplification délibérée, le premier 2-0 de l'utilisateur est utilisé). */
class ClassementsTabViewModel(
    private val listMesDeuxZeroUseCase: ListMesDeuxZeroUseCase,
) : ViewModel() {

    private val _deuxZeroId = MutableStateFlow<String?>(null)
    val deuxZeroId = _deuxZeroId.asStateFlow()

    init {
        viewModelScope.launch {
            listMesDeuxZeroUseCase().onSuccess { deuxZeros ->
                _deuxZeroId.value = deuxZeros?.firstOrNull()?.id
            }
        }
    }
}
