package onboarding.taskmanagement.utils.dtos.out

data class TaskEventDto(
    val taskId: Long,
    val taskType: String,
    val createdAt: String
)