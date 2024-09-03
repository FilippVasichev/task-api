package zionweeds.com.task.auth.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.taskApi.jooq.tables.pojos.User
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.UUID

@Service
class JwtService(
    val userService: UserService,
    val objectMapper: ObjectMapper,
    @Value("\${token.secret}") private val tokenSecret: String,
) {
    fun generateAccessToken(
        email: String,
        password: String,
    ): String {
        val user = userService.findByEmail(email)
        if (user != null) {
            return buildAccessToken(user.id!!)
        }
        throw IllegalArgumentException("Password or Login is incorrect")
    }

    suspend fun generateAccessToken(user: User): String = buildAccessToken(user.id!!)

    fun getTokenSubject(token: String): Subject {
        val subject =
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.toByteArray()))
                .build().parseClaimsJws(token).body.subject
        return objectMapper.readValue(subject, Subject::class.java)
    }

    fun validate(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.toByteArray()))
                .build().parseClaimsJws(token)
            true
        } catch (e: JwtException) {
            false
        }
    }

    private fun buildAccessToken(userId: UUID): String {
        val date: Date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val subject = Subject(userId)
        return Jwts.builder()
            .setSubject(objectMapper.writeValueAsString(subject))
            .setExpiration(date)
            .signWith(Keys.hmacShaKeyFor(tokenSecret.toByteArray()))
            .compact()
    }
}

data class Subject(
    val userId: UUID,
)
