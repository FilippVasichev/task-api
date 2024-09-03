package zionweeds.com.task.auth.storage

import java.security.MessageDigest

private const val ALGORITHM = "SHA-256"

internal fun String.encodePassword(): String =
    MessageDigest.getInstance(ALGORITHM)
        .digest(toByteArray())
        .joinToString("") { "%02x".format(it) }
