package onboarding.taskmanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@EnableKafka
@SpringBootApplication
class TaskmanagementApplication

fun main(args: Array<String>) {
	runApplication<TaskmanagementApplication>(*args)
}
