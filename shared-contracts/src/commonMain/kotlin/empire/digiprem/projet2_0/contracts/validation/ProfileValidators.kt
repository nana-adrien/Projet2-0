package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.profile.UpdateProfileRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.notBlank

val validateUpdateProfileRequest = Validation<UpdateProfileRequest> {
    UpdateProfileRequest::pseudo ifPresent {
        notBlank()
        minLength(3)
        maxLength(30)
    }
}
