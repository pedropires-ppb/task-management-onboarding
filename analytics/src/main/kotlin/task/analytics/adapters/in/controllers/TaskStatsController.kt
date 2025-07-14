package task.analytics.adapters.`in`.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import task.analytics.application.services.TaskStatsService


@RestController
class TaskStatsController(
    private val service: TaskStatsService
) {
    @GetMapping("/task-stats/created")
    fun getCreatedCount(): Long {
        return service.countCreatedTasks()
    }
}