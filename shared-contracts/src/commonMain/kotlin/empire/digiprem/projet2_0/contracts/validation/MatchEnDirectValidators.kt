package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.matchendirect.AjouterEvenementRequest
import empire.digiprem.projet2_0.contracts.matchendirect.DesignerCompositionRequest
import empire.digiprem.projet2_0.contracts.matchendirect.DesignerMarqueurRequest
import empire.digiprem.projet2_0.contracts.matchendirect.PeriodeRequest
import empire.digiprem.projet2_0.contracts.matchendirect.TypeEvenementSaisissable
import io.konform.validation.Validation
import io.konform.validation.constraints.enum
import io.konform.validation.constraints.minimum
import io.konform.validation.constraints.notBlank

val validateDesignerCompositionRequest = Validation<DesignerCompositionRequest> {
    DesignerCompositionRequest::utilisateurId {
        notBlank()
    }
}

val validateDesignerMarqueurRequest = Validation<DesignerMarqueurRequest> {
    DesignerMarqueurRequest::utilisateurId {
        notBlank()
    }
}

val validateAjouterEvenementRequest = Validation<AjouterEvenementRequest> {
    AjouterEvenementRequest::type {
        enum<TypeEvenementSaisissable>()
    }
    AjouterEvenementRequest::minute {
        minimum(0)
    }
    AjouterEvenementRequest::periode {
        minimum(1)
    }
}

val validatePeriodeRequest = Validation<PeriodeRequest> {
    PeriodeRequest::periodeTerminee {
        minimum(1)
    }
}
