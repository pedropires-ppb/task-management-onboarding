package user.registration.utils.mappers

import user.registration.domain.entities.user.User
import user.registration.utils.dtos.out.UserDto

object UserMapper {

    fun toDto(user : User) : UserDto {
        return UserDto(
            userId = user.userId,
            username = user.username,
            email = user.email,
            createdAt = user.createdAt.toString()
        )
    }

}