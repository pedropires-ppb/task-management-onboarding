package onboarding.taskmanagement.adapters.out.factories

import onboarding.taskmanagement.domain.entities.task.Task
import onboarding.taskmanagement.domain.entities.task.TaskStatus
import onboarding.taskmanagement.domain.factories.ITaskFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class TaskFactoryImpl : ITaskFactory {

    override fun createTask(
        name: String,
        estimatedTimeInMinutes: Int,
        description: String?,
        label: String?,
        parentTaskId: Long?,
        userId: Long
    ): Task {
        val now = Instant.now()
        val taskStatus = TaskStatus.PENDING
        return Task(
            taskId = generateTaskId(),
            name = name,
            estimatedTimeInMinutes = estimatedTimeInMinutes,
            status = taskStatus,
            description = description,
            label = label,
            parentTaskId = parentTaskId,
            userId = userId,
            createdAt = now,
            updatedAt = now
        )
    }

    private fun generateTaskId(): Long {
        return UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
    }}