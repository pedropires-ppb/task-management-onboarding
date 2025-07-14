package user.registration.domain.factories

import user.registration.domain.entities.user.User

interface IUserFactory {

    fun createUser(
        username: String,
        email: String,
        password: String
    ): User


}