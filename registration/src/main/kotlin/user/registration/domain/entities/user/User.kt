package user.registration.domain.entities.user

import java.time.Instant

data class User(
    val userId: Long,
    val username: String,
    val email: String,
    val password: String,
    val createdAt: Instant,
)
