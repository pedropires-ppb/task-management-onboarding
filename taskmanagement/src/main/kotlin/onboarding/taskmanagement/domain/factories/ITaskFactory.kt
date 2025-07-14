package onboarding.taskmanagement.domain.factories

import onboarding.taskmanagement.domain.entities.task.Task

interface ITaskFactory {

    fun createTask(
        name: String,
        estimatedTimeInMinutes: Int,
        description: String? = null,
        label: String? = null,
        parentTaskId: Long? = null,
        userId: Long,
    ): Task


}