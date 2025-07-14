package onboarding.taskmanagement.application.events

import onboarding.taskmanagement.utils.dtos.out.TaskEventDto

interface TaskEventPublisher {
    fun publishTaskEventMetric(event: TaskEventDto)
}