package com.task.service

import com.task.auth.service.JwtService
import com.task.model.AuthenticationContext
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val jwtService: JwtService,
) {
    fun authenticate(token: String): AuthenticationContext {
        val formattedToken = token.replace("Bearer", "")
        if (jwtService.validate(formattedToken)) {
            val userId = jwtService.getTokenSubject(formattedToken).userId
            return AuthenticationContext(userId)
        } else {
            throw IllegalArgumentException()
        }
    }
}
