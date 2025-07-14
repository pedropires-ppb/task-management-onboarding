package task.analytics.domain.repositories

import task.analytics.domain.entities.TaskEvent
import task.analytics.domain.entities.TaskEventType

interface ITaskEventRepository {

    fun countByTaskType(type: TaskEventType): Long
}