package onboarding.taskmanagement.adapters.`in`.controllers

import onboarding.taskmanagement.application.usecases.ITaskService
import onboarding.taskmanagement.utils.dtos.`in`.RequestCreateTaskDto
import onboarding.taskmanagement.utils.dtos.`in`.RequestUpdateTaskDto
import onboarding.taskmanagement.utils.dtos.out.TaskDto
import onboarding.taskmanagement.utils.dtos.out.UpdatedTaskDto
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.time.Instant

@Controller
class TaskController(
    private val taskService: ITaskService
) {

    @QueryMapping
    fun getTaskById(
        @Argument userId: Long,
        @Argument taskId: Long): TaskDto? {
        return taskService.getTaskById(userId, taskId)
    }

    @QueryMapping
    fun getTasksByName(@Argument userId: Long, @Argument name: String): List<TaskDto> {
        return taskService.getTasksByName(userId, name)
    }

    @QueryMapping
    fun getTasksByLabel(@Argument userId: Long, @Argument label: String): List<TaskDto> {
        return taskService.getTasksByLabel(userId, label)
    }

    @QueryMapping
    fun getTasksByCreatedAt(@Argument userId: Long, @Argument createdAt: Instant): List<TaskDto> {
        return taskService.getTasksByCreatedAt(userId, createdAt)
    }

    @MutationMapping
    fun createTask(@Argument userId: Long, @Argument input: RequestCreateTaskDto): TaskDto {
        return taskService.createTask(userId, input)
    }

    @MutationMapping
    fun updateTask(
        @Argument userId: Long,
        @Argument taskId: Long,
        @Argument input: RequestUpdateTaskDto
    ): UpdatedTaskDto {
        return taskService.updateTask(userId, taskId, input)
    }

    @MutationMapping
    fun deleteTask(@Argument userId: Long, @Argument taskId: Long): Boolean {
        return taskService.deleteTask(userId, taskId)
    }
}