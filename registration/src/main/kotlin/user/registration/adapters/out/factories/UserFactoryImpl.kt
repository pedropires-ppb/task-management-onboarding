package user.registration.adapters.out.factories

import user.registration.domain.entities.user.User
import user.registration.domain.factories.IUserFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class UserFactoryImpl : IUserFactory {


    override fun createUser(
        username: String,
        email: String,
        password: String
    ): User {
        val now = Instant.now()
        return User(
            userId = generateUserId(),
            username = username,
            email = email,
            password = password,
            createdAt = now
        )
    }

    private fun generateUserId(): Long {
        return UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
    }
}