package zionweeds.com.task.auth.registration

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zionweeds.com.task.auth.service.UserService

@RestController
@RequestMapping("/api/v1/registration")
class RegistrationController(
    private val userService: UserService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun registration(
        @Valid @RequestBody request: RegistrationDto,
    ): ResponseEntity<Void> {
        if (userService.findByEmail(request.email) != null) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        userService.add(request.email, request.password, request.name)
        log.info("зарегался юзер с ${request.email}, ${request.password}, ${request.name}")
        return ResponseEntity(HttpStatus.CREATED)
    }
}

data class RegistrationDto(
    val email: String,
    @NotEmpty val password: String,
    val name: String,
)
