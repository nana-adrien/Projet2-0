package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.deuxzero.CreateDeuxZeroRequest
import empire.digiprem.projet2_0.contracts.deuxzero.EngageClubRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.maximum
import io.konform.validation.constraints.minimum
import io.konform.validation.constraints.notBlank

val validateCreateDeuxZeroRequest = Validation<CreateDeuxZeroRequest> {
    CreateDeuxZeroRequest::nom {
        notBlank()
    }
    CreateDeuxZeroRequest::libelleCampA {
        notBlank()
    }
    CreateDeuxZeroRequest::libelleCampB {
        notBlank()
    }
    CreateDeuxZeroRequest::libelleSaison {
        notBlank()
    }
    CreateDeuxZeroRequest::dureeMiTempsMinutes {
        minimum(1)
        maximum(90)
    }
    CreateDeuxZeroRequest::nombreMiTemps {
        minimum(1)
        maximum(4)
    }
    CreateDeuxZeroRequest::nombreMaxJoueursParCamp {
        minimum(1)
        maximum(30)
    }
    CreateDeuxZeroRequest::delaiValidationAutomatiqueHeures {
        minimum(1)
    }
}

val validateEngageClubRequest = Validation<EngageClubRequest> {
    EngageClubRequest::codeEngagement {
        notBlank()
    }
}
