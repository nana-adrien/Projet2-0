package empire.digiprem.projet2_0.feature.auth.config

import empire.digiprem.projet2_0.feature.auth.datasource.AuthKtorDataSource
import empire.digiprem.projet2_0.feature.auth.datasource.IAuthDataSource
import empire.digiprem.projet2_0.feature.auth.login.LoginViewModel
import empire.digiprem.projet2_0.feature.auth.new_password.NewPasswordViewModel
import empire.digiprem.projet2_0.feature.auth.otp_verification.OtpVerificationViewModel
import empire.digiprem.projet2_0.feature.auth.register_profile.RegisterProfileViewModel
import empire.digiprem.projet2_0.feature.auth.repository.AuthRepository
import empire.digiprem.projet2_0.feature.auth.repository.IAuthRepository
import empire.digiprem.projet2_0.feature.auth.signup_contact.SignupContactViewModel
import empire.digiprem.projet2_0.feature.auth.usecase.LoginUseCase
import empire.digiprem.projet2_0.feature.auth.usecase.LoginWithOtpUseCase
import empire.digiprem.projet2_0.feature.auth.usecase.RegisterUseCase
import empire.digiprem.projet2_0.feature.auth.usecase.RequestOtpUseCase
import empire.digiprem.projet2_0.feature.auth.usecase.ResetPasswordUseCase
import empire.digiprem.projet2_0.feature.auth.usecase.VerifyOtpUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinAuthModule = module {
    // Data layer
    singleOf(::AuthKtorDataSource) bind IAuthDataSource::class
    singleOf(::AuthRepository)     bind IAuthRepository::class

    // Domain — use cases
    singleOf(::RequestOtpUseCase)
    singleOf(::VerifyOtpUseCase)
    singleOf(::RegisterUseCase)
    singleOf(::LoginUseCase)
    singleOf(::LoginWithOtpUseCase)
    singleOf(::ResetPasswordUseCase)

    // Presentation — view models
    viewModelOf(::SignupContactViewModel)
    viewModelOf(::OtpVerificationViewModel)
    viewModelOf(::RegisterProfileViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::NewPasswordViewModel)
}
