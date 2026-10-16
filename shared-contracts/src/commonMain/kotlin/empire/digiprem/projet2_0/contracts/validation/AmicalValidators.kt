package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.amical.CreerMatchAmicalRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.notBlank

val validateCreerMatchAmicalRequest = Validation<CreerMatchAmicalRequest> {
    CreerMatchAmicalRequest::nomEquipeInvitee {
        notBlank()
    }
    CreerMatchAmicalRequest::date {
        notBlank()
    }
    CreerMatchAmicalRequest::heure {
        notBlank()
    }
}
