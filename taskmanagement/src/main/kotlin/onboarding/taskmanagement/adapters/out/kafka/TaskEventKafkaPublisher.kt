package onboarding.taskmanagement.adapters.out.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import onboarding.taskmanagement.application.events.TaskEventPublisher
import onboarding.taskmanagement.utils.dtos.out.TaskEventDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class TaskEventKafkaPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : TaskEventPublisher {

    override fun publishTaskEventMetric(taskEvent: TaskEventDto) {
        val json = objectMapper.writeValueAsString(taskEvent)
        kafkaTemplate.send("task.events", json)
        logger.info { "Published task event to Kafka: $json" }
    }

}