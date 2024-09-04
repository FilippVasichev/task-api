package com.task.controller

import com.task.AbstractComponentTest
import com.taskApi.jooq.tables.pojos.User
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be empty`
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders

class UserControllerTest : AbstractComponentTest() {
    @Test
    fun `should get current user profile`()  {
        val (_, email, password, name) =
            createUser(
                "conoha@gmail.com",
                "asdasd123123",
                "orotimaru",
            )
        val token = jwtService.generateAccessToken(email, password)
        Given {
            port(port)
            contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        } When {
            get("$basePath/users/me")
        } Then {
            statusCode(200)
            val userId = extract().body().jsonPath().getUUID("id")
            val user: User? = userService.findById(userId)
            user!!.name.`should be equal to`(name)
            user.email.`should be equal to`(email)
            user.id.`should be equal to`(userId)
        }
    }

    @Test
    fun `should successfully get all users`()  {
        val currentUser = createUser("conoha@gmail.com", "asdasd123123", "orotimaru")
        createUser("conoha1@gmail.com", "asdasd123123", "minato")
        createUser("conoha2@gmail.com", "asdasd123123", "sasuke")
        val currentUserToken = jwtService.generateAccessToken(currentUser.email, currentUser.password)
        Given {
            port(port)
            contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $currentUserToken")
        } When {
            get("$basePath/users")
        } Then {
            statusCode(200)
            val userList = extract().body().jsonPath().getList<UserResponseDto>("")
            userList.`should not be empty`()
            userList.size.`should be equal to`(3)
        }
    }
}
