package onboarding.taskmanagement.utils.dtos.out


data class SubTaskDto(
    val taskId: Long,
    val name: String,
    val estimatedTimeInMinutes: Int,
    val status: String,
    val description: String?,
    val label: String?,
    val createdAt: String,
    val updatedAt: String,
)
