package task.analytics.utils.dtos

data class TaskEventDto(
    val id: Long? = null,
    val taskId: Long,
    val taskType: String,
    val createdAt: String
)
