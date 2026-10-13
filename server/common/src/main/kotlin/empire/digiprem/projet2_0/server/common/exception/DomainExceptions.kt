package empire.digiprem.projet2_0.server.common.exception

class UserAlreadyExistsException : RuntimeException("A user with this phone/email or pseudo already exists")
class UserNotFoundException : RuntimeException("User not found")
class InvalidCredentialsException : RuntimeException("Invalid credentials")
class InvalidTokenException(message: String) : RuntimeException(message)
class EmailNotVerifiedException : RuntimeException("Account has not been verified")
class ValidationException(val errors: List<String>) : RuntimeException(errors.joinToString(", "))
class InvalidOtpException : RuntimeException("The code is invalid or has expired")
class OtpLockedException : RuntimeException("Too many attempts, try again later")
