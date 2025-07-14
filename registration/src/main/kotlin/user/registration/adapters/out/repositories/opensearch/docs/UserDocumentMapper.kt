package user.registration.adapters.out.repositories.opensearch.docs

import user.registration.domain.entities.user.User
import java.time.Instant

object UserDocumentMapper {

    val documentType = UserDocument::class.java

    fun toDocument(user: User): UserDocument =
        UserDocument(
            userId = user.userId,
            username = user.username,
            email = user.email,
            password = user.password,
            createdAt = user.createdAt.toString()
        )

    fun fromDocument(doc: UserDocument): User = User(
        userId = doc.userId,
        username = doc.username,
        email = doc.email,
        password = doc.password ?: "",
        createdAt = Instant.parse(doc.createdAt)
    )
}