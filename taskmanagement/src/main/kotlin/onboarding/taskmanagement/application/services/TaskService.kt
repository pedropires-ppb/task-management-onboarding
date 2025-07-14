package onboarding.taskmanagement.application.services

import mu.KotlinLogging
import onboarding.taskmanagement.application.usecases.ITaskService
import onboarding.taskmanagement.domain.entities.task.Task
import onboarding.taskmanagement.domain.entities.task.TaskStatus
import onboarding.taskmanagement.application.events.TaskEventPublisher
import onboarding.taskmanagement.application.events.TaskEventType
import onboarding.taskmanagement.domain.factories.ITaskFactory
import onboarding.taskmanagement.domain.repositories.ITaskRepository
import onboarding.taskmanagement.utils.dtos.`in`.RequestCreateTaskDto
import onboarding.taskmanagement.utils.dtos.`in`.RequestUpdateTaskDto
import onboarding.taskmanagement.utils.dtos.out.TaskDto
import onboarding.taskmanagement.utils.dtos.out.TaskEventDto
import onboarding.taskmanagement.utils.dtos.out.UpdatedTaskDto
import onboarding.taskmanagement.utils.mappers.TaskMapper
import org.springframework.stereotype.Service
import java.time.Instant


private val logger = KotlinLogging.logger {}

@Service
class TaskService (
    private val taskRepository: ITaskRepository,
    private val taskFactory: ITaskFactory,
    private val taskEventPublisher: TaskEventPublisher,
    ) : ITaskService {


    override fun createTask(requestingUserId: Long, taskData: RequestCreateTaskDto): TaskDto {
        try {
            val task = taskFactory.createTask(
                name = taskData.name,
                estimatedTimeInMinutes = taskData.estimatedTimeInMinutes,
                description = taskData.description,
                label = taskData.label,
                parentTaskId = taskData.parentTaskId,
                userId = requestingUserId
            )

            val savedTask = taskRepository.save(task)
            logger.info { "Task with the ID ${savedTask.taskId} updated." }

            // Kafka event
            publishCreateTaskEvent(task = savedTask)

            val taskDto = TaskMapper.toDto(savedTask)
            return taskDto

        } catch (ex: Exception) {
            logger.error(ex) { "Error creating the task. Message: ${ex.message}" }
            throw ex
        }
    }


    private fun publishCreateTaskEvent(task: Task) {
        try {
            val taskEvent = TaskEventDto(
                taskId = task.taskId,
                taskType = TaskEventType.CREATED.name,
                createdAt = Instant.now().toString()
            )
            taskEventPublisher.publishTaskEventMetric(taskEvent)
            logger.info { "Task created event published for task ID ${task.taskId}." }

        } catch (ex: Exception) {
            logger.error(ex) { "Error publishing task created event for task ID ${task.taskId}. Message: ${ex.message}" }
            throw ex
        }
    }

    private fun publishUpdateTaskEvent(task: Task) {
        try {
            var taskStatus : TaskEventType? = null
            if (task.status == TaskStatus.CANCELLED) {
                taskStatus = TaskEventType.CANCELLED
            } else if (task.status == TaskStatus.COMPLETED) {
                taskStatus = TaskEventType.COMPLETED
            }
            if (taskStatus != null) {
                val taskEvent = TaskEventDto(
                    taskId = task.taskId,
                    taskType = taskStatus.name,
                    createdAt = Instant.now().toString()
                )
                taskEventPublisher.publishTaskEventMetric(taskEvent)
                logger.info { "Task updated event published for task ID ${task.taskId} with status ${task.status}." }
            }
        } catch (ex: Exception) {
            logger.error(ex) { "Error publishing task created event for task ID ${task.taskId}. Message: ${ex.message}" }
            throw ex
        }
    }

    override fun updateTask(requestingUserId: Long, taskId: Long, taskData: RequestUpdateTaskDto): UpdatedTaskDto {
        try {
            var task = findExistingTaskById(taskId)
            validateUserOwnsTask(task, requestingUserId)
            var statusChanged = false

            task = taskData.name?.let { task.updateName(it) } ?: task
            task = taskData.estimatedTimeInMinutes?.let { task.updateEstimatedTime(it) } ?: task
            task = taskData.status?.let {
                val newStatus = TaskStatus.valueOf(it)
                statusChanged = newStatus != task.status
                task.updateStatus(newStatus)
            } ?: task
            task = taskData.description?.let { task.updateDescription(it) } ?: task
            task = taskData.label?.let { task.updateLabel(it) } ?: task
            task = taskData.parentTaskId?.let { task.updateParentTaskId(it) } ?: task

            val taskUpdated = taskRepository.update(task)
            logger.info { "Task with the ID $taskId updated." }

            // Kafka event
            if (statusChanged) {
                publishUpdateTaskEvent(taskUpdated)
            }

            val taskDto = TaskMapper.toUpdatedTaskDto(taskUpdated)
            return taskDto

        } catch (ex: Exception) {
            logger.error(ex) { "Error updating the task with the ID $taskId . Message: ${ex.message}" }
            throw ex
        }
    }

    private fun validateUserOwnsTask(task: Task, userId: Long) {
        if (task.userId != userId) {
            logger.warn { "User with ID $userId is not authorized to access task with ID ${task.taskId}." }
            throw IllegalAccessException("User with ID $userId is not authorized to access task with ID ${task.taskId}.")
        }
    }

    private fun findExistingTaskById(taskId: Long): Task {
        return taskRepository.findById(taskId) ?: run {
            logger.warn { "Task with the ID $taskId not found." }
            throw NoSuchElementException("Task with the ID $taskId not found.")
        }
    }

    override fun deleteTask(requestingUserId: Long, taskId: Long): Boolean {
        try {
            val task = findExistingTaskById(taskId)
            validateUserOwnsTask(task, requestingUserId)

            // If its a main task, delete all its sub-tasks
            if (!checkIfIsSubTask(task)) {
                val subTasks = taskRepository.findSubTasks(taskId, requestingUserId)
                for (subTask in subTasks) {
                    taskRepository.delete(subTask)
                    logger.info { "Sub-task with ID ${subTask.taskId} deleted." }
                }
            }

            taskRepository.delete(task)
            logger.info { "Task with ID $taskId deleted." }

            return true
        } catch (ex: Exception) {
            logger.error(ex) { "Error deleting task $taskId: ${ex.message}" }
            throw ex
        }
    }

    private fun checkIfIsSubTask(task: Task): Boolean {
        return task.parentTaskId != null
    }

    override fun getTaskById(requestingUserId: Long, taskId: Long): TaskDto? {
        try {
            val task = findExistingTaskById(taskId)
            // validateUserOwnsTask(task, requestingUserId)

            val subTasks = taskRepository.findSubTasks(taskId, requestingUserId)
            logger.info { "Task with ID $taskId found with ${subTasks.size} sub-tasks." }

            return TaskMapper.toDto(task, subTasks)
        } catch (ex: Exception) {
            logger.error(ex) { "Error getting task with ID $taskId: ${ex.message}" }
            throw ex
        }
    }

    override fun getTasksByCreatedAt(requestingUserId: Long, createdAt: Instant): List<TaskDto> {
        try {
            val tasks = taskRepository.findTasksByCreatedAt(createdAt, requestingUserId)

            if (tasks.isEmpty()) {
                logger.warn { "No tasks found created at $createdAt." }
            } else {
                logger.info { "${tasks.size} tasks found created at $createdAt." }
            }

            return mapTasksWithSubtasks(tasks, requestingUserId)
        } catch (ex: Exception) {
            logger.error(ex) { "Error getting tasks by createdAt $createdAt: ${ex.message}" }
            throw ex
        }
    }

    override fun getTasksByName(requestingUserId: Long, name: String): List<TaskDto> {
        try {
            val tasks = taskRepository.findTasksByName(name, requestingUserId)

            if (tasks.isEmpty()) {
                logger.warn { "No tasks found with name $name." }
            } else {
                logger.info { "${tasks.size} tasks found with name $name." }
            }

            return mapTasksWithSubtasks(tasks, requestingUserId)
        } catch (ex: Exception) {
            logger.error(ex) { "Error getting tasks by name $name: ${ex.message}" }
            throw ex
        }
    }

    override fun getTasksByLabel(requestingUserId: Long, label: String): List<TaskDto> {
        try {
            val tasks = taskRepository.findTasksByLabel(label, requestingUserId)

            if (tasks.isEmpty()) {
                logger.warn { "No tasks found with label $label." }
            } else {
                logger.info { "${tasks.size} tasks found with label $label." }
            }

            return mapTasksWithSubtasks(tasks, requestingUserId)
        } catch (ex: Exception) {
            logger.error(ex) { "Error getting tasks by label $label: ${ex.message}" }
            throw ex
        }
    }

    private fun mapTasksWithSubtasks(tasks: List<Task>, requestingUserId: Long): List<TaskDto> {
        val result = mutableListOf<TaskDto>()

        for (task in tasks) {
            val subTasks = taskRepository.findSubTasks(task.taskId, requestingUserId)
            val taskDto = TaskMapper.toDto(task, subTasks)
            result.add(taskDto)
        }

        return result
    }
}