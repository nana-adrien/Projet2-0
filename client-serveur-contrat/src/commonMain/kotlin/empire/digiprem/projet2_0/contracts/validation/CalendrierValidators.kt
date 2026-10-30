package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.calendrier.AjouterMatchExceptionnelRequest
import empire.digiprem.projet2_0.contracts.calendrier.ModifierMatchRequest
import empire.digiprem.projet2_0.contracts.calendrier.RepondreConvocationRequest
import empire.digiprem.projet2_0.contracts.calendrier.ReponseConvocation
import empire.digiprem.projet2_0.contracts.calendrier.StatutMatchModifiable
import io.konform.validation.Validation
import io.konform.validation.constraints.enum
import io.konform.validation.constraints.notBlank

val validateAjouterMatchExceptionnelRequest = Validation<AjouterMatchExceptionnelRequest> {
    AjouterMatchExceptionnelRequest::date {
        notBlank()
    }
    AjouterMatchExceptionnelRequest::heure {
        notBlank()
    }
}

// Le cas croisé "motif obligatoire si REPORTE/ANNULE" est contrôlé dans CalendrierService
// (règle inter-champs, hors du périmètre naturel d'une Validation Konform par champ).
val validateModifierMatchRequest = Validation<ModifierMatchRequest> {
    ModifierMatchRequest::nouveauStatut {
        enum<StatutMatchModifiable>()
    }
}

val validateRepondreConvocationRequest = Validation<RepondreConvocationRequest> {
    RepondreConvocationRequest::reponse {
        enum<ReponseConvocation>()
    }
}
