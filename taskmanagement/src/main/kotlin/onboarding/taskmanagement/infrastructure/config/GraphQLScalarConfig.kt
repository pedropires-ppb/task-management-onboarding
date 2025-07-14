package onboarding.taskmanagement.infrastructure.config

import graphql.scalars.ExtendedScalars
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class GraphQLScalarConfig {
    @Bean
    fun dateTimeScalar(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { builder ->
            builder.scalar(ExtendedScalars.DateTime)
        }
    }

}

