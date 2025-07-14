package user.registration.adapters.out.repositories.opensearch.docs

data class UserDocument(
    val userId: Long,
    val username: String,
    val email: String,
    val password: String? = null,
    val createdAt: String
)
