package empire.digiprem.projet2_0.feature.auth.validator

import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.feature.auth.error.EmailValidationError

object EmailValidator {
    private val emailRegex = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")

    fun validate(email: String): Result<Unit, EmailValidationError> = when {
        email.isBlank() -> Result.Failure(EmailValidationError.Empty)
        !emailRegex.matches(email) -> Result.Failure(EmailValidationError.InvalidFormat)
        else -> Result.Success(Unit)
    }
}
