package task.analytics.application.usecases

import task.analytics.domain.entities.TaskEvent
import task.analytics.utils.dtos.TaskEventDto

interface ITaskStatsService {

    fun saveTaskEvent(taskEvent: TaskEventDto): TaskEventDto

    fun countCreatedTasks(): Long
}