package onboarding.taskmanagement.utils.dtos.out


data class TaskDto(
    val taskId: Long,
    val name: String,
    val estimatedTimeInMinutes: Int,
    val status: String,
    val description: String?,
    val label: String?,
    val parentTaskId: Long?,
    var userId: Long,
    val createdAt: String,
    val updatedAt: String,
    val subTasks: List<SubTaskDto> = emptyList()
)
