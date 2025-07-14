package onboarding.taskmanagement.utils.dtos.`in`

data class RequestCreateTaskDto(
    val name: String,
    val estimatedTimeInMinutes: Int,
    val description: String? = null,
    val label: String? = null,
    val parentTaskId: Long? = null
)