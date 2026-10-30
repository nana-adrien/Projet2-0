package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.statistiques.VoteRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.notBlank

/** CU24 — RG-16 : le contrôle "pas pour soi-même" et "présent sur la feuille de match" se fait
 * côté service (il dépend de l'identité du votant et de l'état de la base), pas ici. */
val validateVoteRequest = Validation<VoteRequest> {
    VoteRequest::candidatId {
        notBlank()
    }
}
