package empire.digiprem.projet2_0.server.common.exception

class UserAlreadyExistsException : RuntimeException("A user with this phone/email or pseudo already exists")
class UserNotFoundException : RuntimeException("User not found")
class InvalidCredentialsException : RuntimeException("Invalid credentials")
class InvalidTokenException(message: String) : RuntimeException(message)
class ValidationException(val errors: List<String>) : RuntimeException(errors.joinToString(", "))
class InvalidOtpException : RuntimeException("The code is invalid or has expired")
class OtpLockedException : RuntimeException("Too many attempts, try again later")
class ResourceNotFoundException(message: String) : RuntimeException(message)
class ForbiddenActionException(message: String) : RuntimeException(message)
class ConflictException(message: String) : RuntimeException(message)
