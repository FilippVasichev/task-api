package com.task

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait.forListeningPort
import org.testcontainers.utility.DockerImageName

class PostgreSQLContainerInitializer private constructor() :
    ApplicationContextInitializer<ConfigurableApplicationContext>,
    PostgreSQLContainer<PostgreSQLContainerInitializer>(
        DockerImageName.parse("postgres:15.4")
            .asCompatibleSubstituteFor("postgres"),
    ) {
        companion object {
            private val instance: PostgreSQLContainerInitializer =
                PostgreSQLContainerInitializer()
                    .withDatabaseName("wishlist")
                    .withUsername("wishlist")
                    .withPassword("secret")
                    .waitingFor(forListeningPort())
        }

        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            instance.start()
            TestPropertyValues.of(
                "spring.datasource.url=${instance.jdbcUrl}",
                "spring.datasource.username=${instance.username}",
                "spring.datasource.password=${instance.password}",
            )
                .applyTo(configurableApplicationContext)
        }
    }
