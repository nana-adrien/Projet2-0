package empire.digiprem.projet2_0.feature.auth.new_password

sealed interface NewPasswordEvent {
    data object OnPasswordResetSuccess : NewPasswordEvent
}
