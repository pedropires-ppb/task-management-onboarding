package onboarding.taskmanagement.utils.mappers

import onboarding.taskmanagement.domain.entities.task.Task
import onboarding.taskmanagement.domain.entities.task.TaskStatus
import onboarding.taskmanagement.utils.dtos.out.SubTaskDto
import onboarding.taskmanagement.utils.dtos.out.TaskDto
import onboarding.taskmanagement.utils.dtos.out.UpdatedTaskDto
import java.time.Instant

object TaskMapper {

    fun toDto(task: Task, subTasks: List<Task> = emptyList()): TaskDto =
        TaskDto(
            taskId = task.taskId,
            name = task.name,
            estimatedTimeInMinutes = task.estimatedTimeInMinutes,
            status = task.status.toString(),
            description = task.description,
            label = task.label,
            parentTaskId = task.parentTaskId,
            userId = task.userId,
            createdAt = task.createdAt.toString(),
            updatedAt = task.updatedAt.toString(),
            subTasks = subTasks.map { toSubTaskDto(it) }
        )

    fun fromDto(dto: TaskDto): Task {
        return Task(
            taskId = dto.taskId,
            name = dto.name,
            estimatedTimeInMinutes = dto.estimatedTimeInMinutes,
            status = TaskStatus.valueOf(dto.status),
            description = dto.description,
            label = dto.label,
            parentTaskId = dto.parentTaskId,
            userId = dto.userId,
            createdAt = Instant.parse(dto.createdAt),
            updatedAt = Instant.parse(dto.updatedAt)
        )
    }

    fun toSubTaskDto(task: Task): SubTaskDto {
        return SubTaskDto(
            taskId = task.taskId,
            name = task.name,
            estimatedTimeInMinutes = task.estimatedTimeInMinutes,
            status = task.status.toString(),
            description = task.description,
            label = task.label,
            createdAt = task.createdAt.toString(),
            updatedAt = task.updatedAt.toString()
        )
    }

    fun toUpdatedTaskDto (task: Task): UpdatedTaskDto {
        return UpdatedTaskDto(
            taskId = task.taskId,
            name = task.name,
            estimatedTimeInMinutes = task.estimatedTimeInMinutes,
            status = task.status.toString(),
            description = task.description,
            label = task.label,
            parentTaskId = task.parentTaskId,
            createdAt = task.createdAt.toString(),
            updatedAt = task.updatedAt.toString()
        )
    }
}