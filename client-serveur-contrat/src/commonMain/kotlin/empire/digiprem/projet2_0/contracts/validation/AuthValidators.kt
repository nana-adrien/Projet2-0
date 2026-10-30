package empire.digiprem.projet2_0.contracts.validation

import empire.digiprem.projet2_0.contracts.auth.LoginRequest
import empire.digiprem.projet2_0.contracts.auth.LoginWithOtpRequest
import empire.digiprem.projet2_0.contracts.auth.RefreshTokenRequest
import empire.digiprem.projet2_0.contracts.auth.RegisterRequest
import empire.digiprem.projet2_0.contracts.auth.RequestOtpRequest
import empire.digiprem.projet2_0.contracts.auth.ResetPasswordRequest
import empire.digiprem.projet2_0.contracts.auth.VerifyOtpRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.notBlank
import io.konform.validation.constraints.pattern

private const val OTP_CODE_PATTERN = "\\d{6}"

// La valeur de "purpose" est vérifiée côté service (OtpPurpose.valueOf) — évite de dupliquer
// l'énumération ici, l'erreur sur une valeur inconnue reste une 400 via ValidationException.
val validateRequestOtpRequest = Validation<RequestOtpRequest> {
    RequestOtpRequest::contact {
        notBlank()
    }
    RequestOtpRequest::purpose {
        notBlank()
    }
}

val validateVerifyOtpRequest = Validation<VerifyOtpRequest> {
    VerifyOtpRequest::contact {
        notBlank()
    }
    VerifyOtpRequest::purpose {
        notBlank()
    }
    VerifyOtpRequest::code {
        pattern(OTP_CODE_PATTERN)
    }
}

val validateRegisterRequest = Validation<RegisterRequest> {
    RegisterRequest::contact {
        notBlank()
    }
    RegisterRequest::pseudo {
        notBlank()
        minLength(3)
        maxLength(30)
    }
    RegisterRequest::verificationToken {
        notBlank()
    }
    RegisterRequest::password ifPresent {
        minLength(8)
    }
}

val validateLoginRequest = Validation<LoginRequest> {
    LoginRequest::contact {
        notBlank()
    }
    LoginRequest::password {
        notBlank()
    }
}

val validateLoginWithOtpRequest = Validation<LoginWithOtpRequest> {
    LoginWithOtpRequest::contact {
        notBlank()
    }
    LoginWithOtpRequest::verificationToken {
        notBlank()
    }
}

val validateResetPasswordRequest = Validation<ResetPasswordRequest> {
    ResetPasswordRequest::contact {
        notBlank()
    }
    ResetPasswordRequest::verificationToken {
        notBlank()
    }
    ResetPasswordRequest::newPassword {
        notBlank()
        minLength(8)
    }
}

val validateRefreshTokenRequest = Validation<RefreshTokenRequest> {
    RefreshTokenRequest::refreshToken {
        notBlank()
    }
}
