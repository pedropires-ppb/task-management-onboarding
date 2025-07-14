package user.registration.domain.repositories

import user.registration.domain.entities.user.User
import java.util.Optional

interface IUserRepository {

    fun save(user : User): User

    fun findById(userId: Long): Optional<User>


}