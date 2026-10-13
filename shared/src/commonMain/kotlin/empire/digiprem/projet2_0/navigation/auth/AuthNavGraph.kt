package empire.digiprem.projet2_0.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import empire.digiprem.projet2_0.feature.auth.login.LoginRoot
import empire.digiprem.projet2_0.feature.auth.model.OtpPurpose
import empire.digiprem.projet2_0.feature.auth.new_password.NewPasswordRoot
import empire.digiprem.projet2_0.feature.auth.otp_verification.OtpVerificationRoot
import empire.digiprem.projet2_0.feature.auth.register_profile.RegisterProfileRoot
import empire.digiprem.projet2_0.feature.auth.signup_contact.SignupContactRoot
import empire.digiprem.projet2_0.feature.auth.welcome.WelcomeScreen

fun NavGraphBuilder.authNavGraph(
    navController : NavHostController,
    onAuthSuccess : () -> Unit,
) {
    navigation<AuthNavGraphRoute.Auth>(startDestination = AuthNavGraphRoute.Welcome) {
        composable<AuthNavGraphRoute.Welcome> {
            WelcomeScreen(
                onCreateAccount = { navController.navigate(AuthNavGraphRoute.SignupContact) },
                onLogin         = { navController.navigate(AuthNavGraphRoute.Login) },
            )
        }

        composable<AuthNavGraphRoute.SignupContact> {
            SignupContactRoot(
                onOtpSent = { contact ->
                    navController.navigate(AuthNavGraphRoute.OtpVerification(contact, OtpPurpose.SIGNUP))
                },
            )
        }

        composable<AuthNavGraphRoute.OtpVerification> { backStackEntry ->
            val route = backStackEntry.toRoute<AuthNavGraphRoute.OtpVerification>()
            OtpVerificationRoot(
                contact = route.contact,
                purpose = route.purpose,
                onVerifiedForSignup = { contact, token ->
                    navController.navigate(AuthNavGraphRoute.RegisterProfile(contact, token))
                },
                onVerifiedForPasswordReset = { contact, token ->
                    navController.navigate(AuthNavGraphRoute.NewPassword(contact, token))
                },
                onLoggedIn = onAuthSuccess,
            )
        }

        composable<AuthNavGraphRoute.RegisterProfile> { backStackEntry ->
            val route = backStackEntry.toRoute<AuthNavGraphRoute.RegisterProfile>()
            RegisterProfileRoot(
                contact           = route.contact,
                verificationToken = route.verificationToken,
                onRegisterSuccess = onAuthSuccess,
            )
        }

        composable<AuthNavGraphRoute.Login> {
            LoginRoot(
                onLoginSuccess = onAuthSuccess,
                onOtpRequested = { contact, purpose ->
                    navController.navigate(AuthNavGraphRoute.OtpVerification(contact, purpose))
                },
            )
        }

        composable<AuthNavGraphRoute.NewPassword> { backStackEntry ->
            val route = backStackEntry.toRoute<AuthNavGraphRoute.NewPassword>()
            NewPasswordRoot(
                contact           = route.contact,
                verificationToken = route.verificationToken,
                onPasswordResetSuccess = {
                    navController.navigate(AuthNavGraphRoute.Login) {
                        popUpTo(AuthNavGraphRoute.Auth) { inclusive = false }
                    }
                },
            )
        }
    }
}
