package onboarding.taskmanagement.domain.entities.task

import java.time.Instant

data class Task (
    val taskId: Long,
    val name: String,
    val estimatedTimeInMinutes: Int,
    val status: TaskStatus = TaskStatus.PENDING,
    val description: String? = null,
    val label: String? = null,
    val parentTaskId: Long? = null, // Only for sub-tasks. This is the ID of the parent task
    val userId: Long,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
{

    init {
        require(name.isNotBlank()) { "Task name cannot be blank" }
        require(estimatedTimeInMinutes > 0) { "Estimated time must be greater than zero" }
    }

    fun updateName(newName: String): Task {
        if (newName.isBlank()) {
            throw IllegalArgumentException("Task name cannot be blank")
        }
        return this.copy(name = newName, updatedAt = Instant.now())
    }

    fun updateEstimatedTime(newEstimatedTime: Int): Task {
        if (newEstimatedTime <= 0) {
            throw IllegalArgumentException("Estimated time must be greater than zero")
        }
        return this.copy(estimatedTimeInMinutes = newEstimatedTime, updatedAt = Instant.now())
    }

    fun updateStatus(newStatus: TaskStatus): Task {
        return this.copy(status = newStatus, updatedAt = Instant.now())
    }

    fun updateDescription(newDescription: String?): Task {
        return this.copy(description = newDescription, updatedAt = Instant.now())
    }

    fun updateLabel(newLabel: String?): Task {
        return this.copy(label = newLabel, updatedAt = Instant.now())
    }

    fun updateParentTaskId(newParentTaskId: Long?): Task {
        if (newParentTaskId == this.taskId) {
            throw IllegalArgumentException("Parent task ID cannot be the same as the task ID")
        }
        return this.copy(parentTaskId = newParentTaskId, updatedAt = Instant.now())
    }

}