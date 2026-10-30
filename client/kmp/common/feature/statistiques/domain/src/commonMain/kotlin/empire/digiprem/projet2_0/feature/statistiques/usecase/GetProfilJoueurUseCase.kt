package empire.digiprem.projet2_0.feature.statistiques.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.statistiques.error.StatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.error.toStatistiquesError
import empire.digiprem.projet2_0.feature.statistiques.model.ContexteStats
import empire.digiprem.projet2_0.feature.statistiques.model.ProfilJoueur
import empire.digiprem.projet2_0.feature.statistiques.repository.IStatistiquesRepository

/** E19 — Profil et progression (CU23). */
class GetProfilJoueurUseCase(private val repository: IStatistiquesRepository) {
    suspend operator fun invoke(utilisateurId: String, deuxZeroId: String, contexte: ContexteStats): Result<ProfilJoueur?, StatistiquesError> =
        repository.getProfilJoueur(utilisateurId, deuxZeroId, contexte).mapFailure { it.toStatistiquesError() }
}
