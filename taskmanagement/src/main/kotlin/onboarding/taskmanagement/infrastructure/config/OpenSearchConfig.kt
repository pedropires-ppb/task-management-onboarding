package onboarding.taskmanagement.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import mu.KotlinLogging
import org.apache.http.HttpHost
import org.opensearch.client.RestClient
import org.opensearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder


@Configuration
class OpenSearchConfig {

    @Value("\${opensearch.host}")
    lateinit var host: String

    @Value("\${opensearch.username}")
    lateinit var username: String

    @Value("\${opensearch.password}")
    lateinit var password: String

    @Bean
    fun openSearchClient(): RestHighLevelClient {
        val credsProvider = BasicCredentialsProvider()
        credsProvider.setCredentials(
            AuthScope.ANY, // Allow any host and port
            UsernamePasswordCredentials(username, password)
        )

        return RestHighLevelClient(
            RestClient.builder(HttpHost.create(host))
                .setHttpClientConfigCallback { httpClientBuilder: HttpAsyncClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(credsProvider)
                }
        )
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerModule(kotlinModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    }
}