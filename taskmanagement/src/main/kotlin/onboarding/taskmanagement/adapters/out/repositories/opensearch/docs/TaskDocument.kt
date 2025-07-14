package onboarding.taskmanagement.adapters.out.repositories.opensearch.docs

import onboarding.taskmanagement.domain.entities.task.TaskStatus

data class TaskDocument(
    val taskId: Long,
    val name: String,
    val estimatedTimeInMinutes: Int,
    val status: TaskStatus,
    val description: String?,
    val label: String?,
    val parentTaskId: Long?,
    val userId: Long,
    val createdAt: String,
    val updatedAt: String
)
