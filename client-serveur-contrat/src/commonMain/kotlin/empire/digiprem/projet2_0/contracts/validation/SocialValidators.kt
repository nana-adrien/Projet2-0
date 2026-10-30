package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.social.SignalerRequest
import empire.digiprem.projet2_0.contracts.social.TypeContenuSignale
import io.konform.validation.Validation
import io.konform.validation.constraints.enum
import io.konform.validation.constraints.notBlank

val validateSignalerRequest = Validation<SignalerRequest> {
    SignalerRequest::contenuType {
        enum<TypeContenuSignale>()
    }
    SignalerRequest::contenuId {
        notBlank()
    }
    SignalerRequest::motif {
        notBlank()
    }
}
