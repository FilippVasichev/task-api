package zionweeds.com.task.auth.service

import com.taskApi.jooq.tables.pojos.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zionweeds.com.task.auth.storage.UserStorage
import java.util.UUID

@Service
class UserService(
    val userStorage: UserStorage,
) {
    @Transactional
    fun add(
        email: String,
        password: String,
        name: String,
    ) {
        userStorage.insert(email, password, name)
    }

    fun findByEmail(email: String): User? = userStorage.findByEmail(email)

    fun findById(id: UUID): User? = userStorage.findById(id)
}
