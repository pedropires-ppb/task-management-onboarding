package task.analytics.adapters.`in`.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import task.analytics.application.usecases.ITaskStatsService
import task.analytics.utils.dtos.TaskEventDto

private val logger = KotlinLogging.logger {}

@Component
class TaskEventKafkaListener(
    private val service: ITaskStatsService,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(topics = ["task.events"], groupId = "task-events-consumer")
    fun consume(message: String) {
        try {
            val eventDto = objectMapper.readValue(message, TaskEventDto::class.java)
            service.saveTaskEvent(eventDto)
            logger.info { "Task event processed successfully: $eventDto" }
        } catch (e: Exception) {
            logger.error(e) { "Error processing task event message: $message" }
            e.printStackTrace()
        }
    }
}