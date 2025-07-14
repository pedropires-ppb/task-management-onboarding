package onboarding.taskmanagement.utils.dtos.`in`

data class RequestUpdateTaskDto(
    val name: String? = null,
    val estimatedTimeInMinutes: Int? = null,
    val status: String? = null,
    val description: String? = null,
    val label: String? = null,
    val parentTaskId: Long? = null
)
