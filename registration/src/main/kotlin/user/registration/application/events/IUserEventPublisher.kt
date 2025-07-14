package user.registration.application.events

import user.registration.utils.dtos.`in`.UserRegisteredEventDto

interface IUserEventPublisher {
    fun publishUserRegistered(event: UserRegisteredEventDto)
}