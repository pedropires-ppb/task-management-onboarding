package user.registration.adapters.`in`.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import user.registration.application.usecases.IUserService
import user.registration.utils.dtos.`in`.UserRegisteredEventDto
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UserKafkaConsumer(
    private val userService: IUserService,
) {

    /**
     *  Assuming user data came from security service that handles user registration and password encryption
     */
    @KafkaListener(topics = ["user.created"], groupId = "user-events")
    fun createNewUser(userRegisteredEventDto: UserRegisteredEventDto) {
        try {
            userService.createUser(userRegisteredEventDto)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}