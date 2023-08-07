package dev.dizyaa.dizgram.feature.user.domain

import dev.dizyaa.dizgram.feature.chat.domain.SenderId
import org.drinkless.td.libcore.telegram.TdApi

data class User(
    val userId: UserId,
)

fun TdApi.User.toDomain(): User {
    return User(
        userId = UserId(this.id),
    )
}

data class UserId(val value: Long): SenderId