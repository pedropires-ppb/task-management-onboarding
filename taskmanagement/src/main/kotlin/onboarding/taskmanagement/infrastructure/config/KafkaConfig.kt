package onboarding.taskmanagement.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.apache.kafka.common.errors.SerializationException
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff
@Configuration
class KafkaConfig {


//    @Bean
//    fun kafkaErrorHandler(): DefaultErrorHandler {
//        val backOff = FixedBackOff(3000L, 3)
//        val errorHandler = DefaultErrorHandler(backOff)
//        errorHandler.addNotRetryableExceptions(SerializationException::class.java)
//        return errorHandler
//    }
}
