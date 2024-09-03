package zionweeds.com.task.auth.storage

import com.taskApi.jooq.tables.pojos.User
import com.taskApi.jooq.tables.records.UserRecord
import com.taskApi.jooq.tables.references.USER
import org.jooq.DSLContext
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserStorage(
    val dslContext: DSLContext,
) {
    fun insert(
        email: String,
        password: String,
        name: String,
    ): User =
        dslContext.insertInto(USER)
            .set(USER.EMAIL, email)
            .set(USER.PASSWORD, password.encodePassword())
            .set(USER.NAME, name)
            .returningResult(USER.ID)
            .map {
                User(
                    id = it.get(USER.ID),
                    email = email,
                    password = password,
                    name = name,
                )
            }.first()

    fun findByEmail(email: String): User? = dslContext.selectFrom(USER).where(USER.EMAIL.eq(email)).map(this::map).firstOrNull()

    fun findById(id: UUID): User? = dslContext.selectFrom(USER).where(USER.ID.eq(id)).map(this::map).firstOrNull()

    private fun map(record: UserRecord) =
        User(
            id = record.id,
            email = record.email,
            password = record.password,
            name = record.name,
        )
}
