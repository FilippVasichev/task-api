package com.task

import com.task.auth.service.JwtService
import com.task.auth.service.UserService
import com.taskApi.jooq.tables.pojos.User
import com.taskApi.jooq.tables.references.USER
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ContextConfiguration(
    initializers = [
        PostgreSQLContainerInitializer::class,
    ],
)
abstract class AbstractComponentTest {
    val basePath = "/api/v1"

    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var dslContext: DSLContext

    @AfterEach
    fun cleanUpDb() {
        dslContext.truncate(USER).cascade()
    }

    fun createUser(
        email: String,
        password: String,
        name: String,
    ): User = userService.add(email, password, name)
}
