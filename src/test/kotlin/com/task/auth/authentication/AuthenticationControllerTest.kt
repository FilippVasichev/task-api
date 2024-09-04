package com.task.auth.authentication

import com.task.AbstractComponentTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class AuthenticationControllerTest : AbstractComponentTest() {
    @Test
    fun `should get HTTP 403 with un existing email`()  {
        Given {
            port(port)
            contentType(ContentType.JSON)
                .body(
                    UserCredentialsRequestDto(
                        email = "unexisted@gmail.com",
                        password = "asdasd12123",
                    ),
                )
        } When {
            post("$basePath/authentication")
        } Then {
            statusCode(HttpStatus.FORBIDDEN.value())
            body("message", equalTo("login or password is incorrect"))
        }
    }
}
