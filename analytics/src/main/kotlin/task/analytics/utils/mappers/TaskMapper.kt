package task.analytics.utils.mappers

import task.analytics.domain.entities.TaskEvent
import task.analytics.domain.entities.TaskEventType
import task.analytics.utils.dtos.TaskEventDto
import java.time.Instant

object TaskMapper {

    fun toDto(taskEvent: TaskEvent): TaskEventDto {
        return TaskEventDto(
            id = taskEvent.id,
            taskId = taskEvent.taskId,
            taskType = taskEvent.taskType.name,
            createdAt = taskEvent.createdAt.toString()
        )
    }

    fun toDomain(dto: TaskEventDto): TaskEvent {
        return TaskEvent(
            id = dto.id,
            taskId = dto.taskId,
            taskType = TaskEventType.valueOf(dto.taskType),
            createdAt = Instant.parse(dto.createdAt)
        )
    }
}