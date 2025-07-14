package onboarding.taskmanagement.application.usecases

import onboarding.taskmanagement.utils.dtos.`in`.RequestCreateTaskDto
import onboarding.taskmanagement.utils.dtos.`in`.RequestUpdateTaskDto
import onboarding.taskmanagement.utils.dtos.out.TaskDto
import onboarding.taskmanagement.utils.dtos.out.UpdatedTaskDto
import java.time.Instant

interface ITaskService {

    fun createTask(requestingUserId: Long, taskData: RequestCreateTaskDto): TaskDto

    fun updateTask(requestingUserId: Long, taskId: Long, taskData: RequestUpdateTaskDto): UpdatedTaskDto

    fun deleteTask(requestingUserId: Long, taskId: Long) : Boolean

    fun getTaskById(requestingUserId: Long, taskId: Long): TaskDto?

    fun getTasksByCreatedAt(requestingUserId: Long, createdAt: Instant): List<TaskDto>

    fun getTasksByName(requestingUserId: Long, name: String): List<TaskDto>

    fun getTasksByLabel(requestingUserId: Long, label: String): List<TaskDto>
}