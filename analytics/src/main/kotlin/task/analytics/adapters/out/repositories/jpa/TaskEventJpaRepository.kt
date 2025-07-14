package task.analytics.adapters.out.repositories.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import task.analytics.adapters.out.repositories.entities.TaskEventEntity
import task.analytics.domain.entities.TaskEventType

@Repository
interface TaskEventJpaRepository : JpaRepository<TaskEventEntity, Long> {

    @Query("SELECT COUNT(e) FROM TaskEventEntity e WHERE e.taskType = :type")
    fun countByTaskType(type: TaskEventType): Long
}