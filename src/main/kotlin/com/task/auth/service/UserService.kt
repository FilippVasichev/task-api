package com.task.auth.service

import com.task.auth.storage.UserStorage
import com.task.auth.storage.encodePassword
import com.task.model.UserModel
import com.taskApi.jooq.tables.pojos.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userStorage: UserStorage,
) {
    @Transactional
    fun add(
        email: String,
        password: String,
        name: String,
    ): User = userStorage.insert(email, password, name)

    fun findAll(): List<UserModel> = userStorage.findAll()

    fun findByEmail(email: String): User? = userStorage.findByEmail(email)

    fun findById(id: UUID): User? = userStorage.findById(id)

    fun isPasswordValid(
        providedPassword: String,
        actualPassword: String,
    ) = providedPassword.encodePassword() == actualPassword
}
