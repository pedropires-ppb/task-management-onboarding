package task.analytics.adapters.out.repositories.jpa.impl

import org.springframework.stereotype.Repository
import task.analytics.adapters.out.repositories.jpa.TaskEventJpaRepository
import task.analytics.domain.entities.TaskEventType
import task.analytics.domain.repositories.ITaskEventRepository

@Repository
class TaskEventRepositoryImpl(
    private val jpaRepository: TaskEventJpaRepository
) : ITaskEventRepository {

    override fun countByTaskType(type: TaskEventType): Long {
        return jpaRepository.countByTaskType(type)
    }
}