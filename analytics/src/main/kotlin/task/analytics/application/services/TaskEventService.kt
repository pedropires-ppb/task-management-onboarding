package task.analytics.application.services

import mu.KotlinLogging
import org.springframework.stereotype.Service
import task.analytics.adapters.out.repositories.entities.TaskEventEntity
import task.analytics.adapters.out.repositories.jpa.TaskEventJpaRepository
import task.analytics.application.usecases.ITaskStatsService
import task.analytics.domain.entities.TaskEvent
import task.analytics.domain.entities.TaskEventType
import task.analytics.utils.dtos.TaskEventDto
import task.analytics.utils.mappers.TaskMapper

private val logger = KotlinLogging.logger {}

@Service
class TaskStatsService(
    private val repository: TaskEventJpaRepository
) : ITaskStatsService {

    override fun saveTaskEvent(taskEvent: TaskEventDto): TaskEventDto {
        try {
            val domainEvent = TaskMapper.toDomain(taskEvent)
            val entity = TaskEventEntity.fromDomain(domainEvent)
            val savedEntity = repository.save(entity)
            logger.info { "Task event saved: $savedEntity" }
            val taskDto = TaskMapper.toDto(savedEntity.toDomain())

            return taskDto

        } catch (e: Exception) {
            logger.error(e) { "Error saving task event" }
            throw e
        }
    }

    override fun countCreatedTasks(): Long {
        try {
            val counter = repository.countByTaskType(TaskEventType.CREATED)
            logger.info { "Count of created tasks: $counter" }
            return counter
        } catch (e: Exception) {
            logger.error(e) { "Error counting created tasks" }
            return 0L
        }
    }

}