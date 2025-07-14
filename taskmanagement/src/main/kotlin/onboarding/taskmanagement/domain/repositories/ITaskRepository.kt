package onboarding.taskmanagement.domain.repositories

import onboarding.taskmanagement.domain.entities.task.Task
import java.time.Instant
import java.util.Optional

interface ITaskRepository {

    fun save(task: Task): Task

    fun delete(task: Task)

    fun update(task: Task): Task

    fun findById(taskId: Long): Task?

    fun findTasksByCreatedAt(createdAt: Instant, userId: Long): List<Task>

    fun findTasksByName(name: String, userId: Long): List<Task>

    fun findTasksByLabel(label: String, userId: Long): List<Task>

    fun findSubTasks(parentTaskId: Long, userId: Long): List<Task>

}