package task.analytics.domain.entities

import java.time.Instant

data class TaskEvent(
    val id: Long? = null,
    val taskId: Long,
    val taskType: TaskEventType,
    val createdAt: Instant
)