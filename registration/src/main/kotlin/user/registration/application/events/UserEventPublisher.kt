package user.registration.application.events

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import user.registration.utils.dtos.`in`.UserRegisteredEventDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class UserEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : IUserEventPublisher {

    override fun publishUserRegistered(event: UserRegisteredEventDto) {
        val json = objectMapper.writeValueAsString(event)
        kafkaTemplate.send("user.registration", json)
        logger.info { "Published user registration event to Kafka: $json" }
    }
}