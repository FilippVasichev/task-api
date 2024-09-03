package zionweeds.com.task.auth.authentication

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zionweeds.com.task.auth.service.JwtService

@RestController
@RequestMapping("/api/v1/authentication")
class AuthenticationController(
    private val jwtService: JwtService,
) {
    @PostMapping
    fun authentication(
        @Valid @RequestBody request: UserCredentialsRequestDto,
    ): ResponseEntity<AuthenticationDto> {
        val token = jwtService.generateAccessToken(request.email, request.password)
        return ResponseEntity.ok(AuthenticationDto(token))
    }
}

data class UserCredentialsRequestDto(
    val email: String,
    val password: String,
)

data class AuthenticationDto(
    val accessToken: String,
)
