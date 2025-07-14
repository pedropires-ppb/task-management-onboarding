package onboarding.taskmanagement.adapters.out.repositories.opensearch.docs

import onboarding.taskmanagement.domain.entities.task.Task
import java.time.Instant

object TaskDocumentMapper {

    val documentType = TaskDocument::class.java

    fun toDocument(task: Task): TaskDocument = TaskDocument(
        taskId = task.taskId,
        name = task.name,
        estimatedTimeInMinutes = task.estimatedTimeInMinutes,
        status = task.status,
        description = task.description,
        label = task.label,
        parentTaskId = task.parentTaskId,
        userId = task.userId,
        createdAt = task.createdAt.toString(),
        updatedAt = task.updatedAt.toString()
    )

    fun fromDocument(doc: TaskDocument): Task = Task(
        taskId = doc.taskId,
        name = doc.name,
        estimatedTimeInMinutes = doc.estimatedTimeInMinutes,
        status = doc.status,
        description = doc.description,
        label = doc.label,
        parentTaskId = doc.parentTaskId,
        userId = doc.userId,
        createdAt = Instant.parse(doc.createdAt),
        updatedAt = Instant.parse(doc.updatedAt)
    )
}