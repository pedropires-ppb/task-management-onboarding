package user.registration.utils.dtos.`in`


data class UserRegisteredEventDto(
    val username: String,
    val email: String,
    val encryptedPassword: String
)