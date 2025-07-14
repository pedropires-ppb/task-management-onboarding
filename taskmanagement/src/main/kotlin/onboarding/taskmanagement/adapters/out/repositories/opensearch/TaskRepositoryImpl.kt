package onboarding.taskmanagement.adapters.out.repositories.opensearch

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import onboarding.taskmanagement.adapters.out.repositories.opensearch.docs.TaskDocumentMapper
import onboarding.taskmanagement.domain.entities.task.Task
import onboarding.taskmanagement.domain.repositories.ITaskRepository
import org.opensearch.action.delete.DeleteRequest
import org.opensearch.action.get.GetRequest
import org.opensearch.action.index.IndexRequest
import org.opensearch.action.search.SearchRequest
import org.opensearch.action.update.UpdateRequest
import org.opensearch.client.RequestOptions
import org.opensearch.client.RestHighLevelClient
import org.opensearch.index.query.QueryBuilder
import org.opensearch.index.query.QueryBuilders
import org.opensearch.search.builder.SearchSourceBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class TaskRepositoryImpl(
    private val client: RestHighLevelClient,
    private val objectMapper: ObjectMapper
) : ITaskRepository {

    @Value("\${opensearch.index.tasks}")
    private lateinit var indexName: String

    // Helper to convert between Map<String, Any> and TaskDocument
    companion object {
        private val mapStringAnyType = object : TypeReference<Map<String, Any>>() {}
    }

    override fun save(task: Task): Task {
        val doc = TaskDocumentMapper.toDocument(task)
        val request = IndexRequest(indexName)
            .id(doc.taskId.toString())
            .source(objectMapper.convertValue(doc, mapStringAnyType))

        client.index(request, RequestOptions.DEFAULT)
        return task
    }

    override fun delete(task: Task) {
        val request = DeleteRequest(indexName, task.taskId.toString())
        client.delete(request, RequestOptions.DEFAULT)
    }

    override fun update(task: Task): Task {
        val doc = TaskDocumentMapper.toDocument(task)
        val request = UpdateRequest(indexName, doc.taskId.toString())
            .doc(objectMapper.convertValue(doc, mapStringAnyType))

        client.update(request, RequestOptions.DEFAULT)
        return task
    }

    override fun findById(taskId: Long): Task? {
        val request = GetRequest(indexName, taskId.toString())
        val response = client.get(request, RequestOptions.DEFAULT)

        return if (response.isExists) {
            val doc = objectMapper.convertValue(response.sourceAsMap, TaskDocumentMapper.documentType)
            TaskDocumentMapper.fromDocument(doc)
        } else {
            null
        }
    }

    override fun findTasksByCreatedAt(createdAt: Instant, userId: Long): List<Task> {
        return findManyByUserIdAndField("createdAt", createdAt.toString(), userId)
    }

    override fun findTasksByName(name: String, userId: Long): List<Task> {
        return findManyByUserIdAndField("name", name, userId)
    }

    override fun findTasksByLabel(label: String, userId: Long): List<Task> {
        return findManyByUserIdAndField("label", label, userId)
    }

    override fun findSubTasks(parentTaskId: Long, userId: Long): List<Task> {
        return findManyByUserIdAndField("parentTaskId", parentTaskId, userId)
    }

    /**
     * Helper method to find many tasks by a given query.
     *
     */
    private fun findManyByUserIdAndField(field: String, value: Any, userId: Long): List<Task> {
        val boolQuery = QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery("userId", userId))
            .must(QueryBuilders.wildcardQuery("$field.keyword", "*${value}*"))

        val searchRequest = SearchRequest(indexName)
        searchRequest.source(
            SearchSourceBuilder()
                .query(boolQuery)
                .size(1000)
        )

        val response = client.search(searchRequest, RequestOptions.DEFAULT)

        val tasks = mutableListOf<Task>()
        for (task in response.hits.hits) {
            val doc = objectMapper.convertValue(task.sourceAsMap, TaskDocumentMapper.documentType)
            tasks.add(TaskDocumentMapper.fromDocument(doc))
        }

        return tasks
    }
}