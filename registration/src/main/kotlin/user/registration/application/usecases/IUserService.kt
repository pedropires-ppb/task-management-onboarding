package user.registration.application.usecases

import user.registration.utils.dtos.`in`.UserRegisteredEventDto
import user.registration.utils.dtos.out.UserDto

interface IUserService {

    fun createUser(userData: UserRegisteredEventDto): UserDto

    fun findById(userId: Long): UserDto?
}