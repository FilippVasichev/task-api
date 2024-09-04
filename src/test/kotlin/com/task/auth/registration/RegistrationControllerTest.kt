package com.task.auth.registration

import com.task.AbstractComponentTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.amshove.kluent.`should not be null`
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class RegistrationControllerTest : AbstractComponentTest() {
    @Test
    fun `should return HTTP 500 when registering with an already taken email`()  {
        val (_, email, password, name) =
            createUser(email = "email@gmail.com", password = "password123", name = "name")
        Given {
            port(port)
            contentType(ContentType.JSON)
                .body(RegistrationDto(email, password, name))
        } When {
            post("$basePath/registration")
        } Then {
            statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        }
    }

    @Test
    fun `should successfully create user`()  {
        val email = "email@gmail.com"
        Given {
            port(port)
            contentType(ContentType.JSON)
                .body(
                    RegistrationDto(
                        email,
                        password = "asdasd123123",
                        name = "tester",
                    ),
                )
        } When {
            post("$basePath/registration")
        } Then {
            statusCode(HttpStatus.CREATED.value())
            userService.findByEmail(email).`should not be null`()
        }
    }
}
