package user.registration.application.services

import mu.KotlinLogging
import user.registration.application.usecases.IUserService
import user.registration.domain.factories.IUserFactory
import user.registration.domain.repositories.IUserRepository
import user.registration.utils.dtos.`in`.UserRegisteredEventDto
import user.registration.utils.dtos.out.UserDto
import user.registration.utils.mappers.UserMapper
import org.springframework.stereotype.Service


private val logger = KotlinLogging.logger {}

@Service
class UserService (
    private val userRepository: IUserRepository,
    private val userFactory: IUserFactory,
    ) : IUserService {

    override fun createUser(userData: UserRegisteredEventDto): UserDto {
        try {

            val user = userFactory.createUser(
                username = userData.username,
                email = userData.email,
                password = userData.encryptedPassword
            )
            val savedUser = userRepository.save(user)
            logger.info { "User with the ID ${savedUser.userId} created." }

            val userDto = UserMapper.toDto(savedUser)
            return userDto

        } catch (ex: Exception) {
            logger.error(ex) { "Error creating the user. Message: ${ex.message}" }
            throw ex
        }
    }


    override fun findById(userId: Long): UserDto? {
        try {
            val user = userRepository.findById(userId)
            if (user.isPresent) {
                val userDto = UserMapper.toDto(user.get())
                logger.info { "User with ID $userId found." }

                return userDto
            } else {
                logger.warn { "User with ID $userId not found." }
                return null
            }
        } catch (ex: Exception) {
            logger.error(ex) { "Error finding user with ID $userId. Message: ${ex.message}" }
            throw ex
        }
    }


}