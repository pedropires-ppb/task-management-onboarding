package user.registration.adapters.out.repositories.opensearch

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import user.registration.adapters.out.repositories.opensearch.docs.UserDocumentMapper
import user.registration.domain.entities.user.User
import user.registration.domain.repositories.IUserRepository
import org.opensearch.action.get.GetRequest
import org.opensearch.action.index.IndexRequest
import org.opensearch.client.RequestOptions
import org.opensearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class UserRepositoryImpl(
    private val client: RestHighLevelClient,
    private val objectMapper: ObjectMapper
) : IUserRepository {

    @Value("\${opensearch.index.users}")
    private lateinit var indexName: String

    // Helper to convert between Map<String, Any> and TaskDocument
    companion object {
        private val mapStringAnyType = object : TypeReference<Map<String, Any>>() {}
    }

    override fun save(user: User): User {
        val doc = UserDocumentMapper.toDocument(user)
        val request = IndexRequest(indexName)
            .id(doc.userId.toString())
            .source(objectMapper.convertValue(doc, mapStringAnyType))

        client.index(request, RequestOptions.DEFAULT)
        return user
    }

    override fun findById(userId: Long): Optional<User> {
        val request = GetRequest(indexName, userId.toString())
        val response = client.get(request, RequestOptions.DEFAULT)

        return if (response.isExists) {
            val doc = objectMapper.convertValue(response.sourceAsMap, UserDocumentMapper.documentType)
            Optional.of(UserDocumentMapper.fromDocument(doc))
        } else {
            Optional.empty()
        }
    }

}