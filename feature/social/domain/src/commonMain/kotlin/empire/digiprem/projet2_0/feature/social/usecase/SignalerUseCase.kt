package empire.digiprem.projet2_0.feature.social.usecase

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.mapFailure
import empire.digiprem.projet2_0.feature.social.error.SocialError
import empire.digiprem.projet2_0.feature.social.error.toSocialError
import empire.digiprem.projet2_0.feature.social.model.Signalement
import empire.digiprem.projet2_0.feature.social.model.TypeContenuSignale
import empire.digiprem.projet2_0.feature.social.repository.ISocialRepository

/** CU27 — Signaler un contenu ou un comportement (F-SOC-05). */
class SignalerUseCase(private val repository: ISocialRepository) {
    suspend operator fun invoke(contenuType: TypeContenuSignale, contenuId: String, motif: String): Result<Signalement?, SocialError> =
        repository.signaler(contenuType, contenuId, motif).mapFailure { it.toSocialError() }
}
