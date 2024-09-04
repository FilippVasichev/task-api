package com.task.controller

import com.task.auth.service.UserService
import com.task.service.AuthenticationService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
) {
    @GetMapping("/me")
    fun me(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
    ): UserResponseDto =
        authenticationService.authenticate(token).let {
            userService.findById(it.userId).let { currentUser ->
                UserResponseDto(
                    id = currentUser!!.id.toString(),
                    email = currentUser.email,
                    name = currentUser.name,
                )
            }
        }

    @GetMapping
    fun allUsers(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
    ): List<UserResponseDto> =
        authenticationService.authenticate(token).let {
            val users = userService.findAll()
            return users.map {
                UserResponseDto(
                    id = it.id,
                    email = it.email,
                    name = it.name,
                )
            }
        }
}

data class UserResponseDto(
    val id: String,
    val email: String,
    val name: String,
)
