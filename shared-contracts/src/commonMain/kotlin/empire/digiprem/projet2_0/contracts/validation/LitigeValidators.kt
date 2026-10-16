package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.litige.ContesterRequest
import empire.digiprem.projet2_0.contracts.litige.RepondreContestationRequest
import empire.digiprem.projet2_0.contracts.litige.TrancherLitigeRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.enum
import io.konform.validation.constraints.notBlank

/** Décisions possibles pour trancher un litige (CU20). Dupliqué depuis
 * `feature/validation/domain/.../DecisionLitige` (mobile) : les validateurs Konform de
 * `:contracts.validation` ne peuvent dépendre d'un module feature. */
private enum class DecisionLitigeSaisissable { GARDER_RESULTAT_SAISI, APPLIQUER_CORRECTION, ANNULER_MATCH }

val validateContesterRequest = Validation<ContesterRequest> {
    ContesterRequest::correctionProposee {
        notBlank()
    }
}

// Pas de contrainte forte nécessaire : `accepter` est un Boolean (toujours valide
// structurellement), `commentaire` est optionnel. Conservé pour suivre le style du module
// (chaque DTO d'écriture a son validateur dédié, même vide).
val validateRepondreContestationRequest = Validation<RepondreContestationRequest> {
}

val validateTrancherLitigeRequest = Validation<TrancherLitigeRequest> {
    TrancherLitigeRequest::decision {
        enum<DecisionLitigeSaisissable>()
    }
    TrancherLitigeRequest::motif {
        notBlank()
    }
}
