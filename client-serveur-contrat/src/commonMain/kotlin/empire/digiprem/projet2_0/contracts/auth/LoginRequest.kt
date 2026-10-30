package empire.digiprem.projet2_0.contracts.auth

import io.konform.validation.Validation
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.notBlank
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val contact: String,
    val password: String,
)

@Serializable
data class LoginWithOtpRequest(
    val contact: String,
    val verificationToken: String,
)
/*{
    companion object {
        val validate = Validation<LoginWithOtpRequest> {
            LoginWithOtpRequest::contact required {
                // On passe un code technique dans le "hint"
                hint="ERROR_CONTACT_NULL"

                notBlank() hint "ERROR_CONTACT_NULL"
            }
            LoginWithOtpRequest::verificationToken {
                constrain("ERROR_TOKEN_REQUIRED"){ it != null }
            }



        }
    }

}*/
