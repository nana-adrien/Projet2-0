package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.club.CreateClubRequest
import empire.digiprem.projet2_0.contracts.club.JoinClubRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.notBlank

val validateCreateClubRequest = Validation<CreateClubRequest> {
    CreateClubRequest::nom {
        notBlank()
        minLength(2)
        maxLength(60)
    }
}

val validateJoinClubRequest = Validation<JoinClubRequest> {
    JoinClubRequest::code {
        notBlank()
    }
}
