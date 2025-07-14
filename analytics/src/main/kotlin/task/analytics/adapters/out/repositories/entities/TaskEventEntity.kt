package task.analytics.adapters.out.repositories.entities

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import task.analytics.domain.entities.TaskEvent
import task.analytics.domain.entities.TaskEventType
import java.time.Instant

@Entity
@Table(name = "task_events")
data class TaskEventEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val taskId: Long,

    @Enumerated(EnumType.STRING)
    val taskType: TaskEventType,

    val createdAt: Instant
) {
    fun toDomain(): TaskEvent = TaskEvent(id, taskId, taskType, createdAt)

    companion object {
        fun fromDomain(event: TaskEvent): TaskEventEntity =
            TaskEventEntity(event.id, event.taskId, event.taskType, event.createdAt)
    }
}