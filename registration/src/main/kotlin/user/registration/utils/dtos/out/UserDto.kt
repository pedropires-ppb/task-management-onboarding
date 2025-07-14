package user.registration.utils.dtos.out


data class UserDto(
    val userId: Long,
    val username: String,
    val email: String,
    val createdAt: String
)
