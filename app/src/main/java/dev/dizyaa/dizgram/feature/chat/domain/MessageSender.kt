package dev.dizyaa.dizgram.feature.chat.domain

import dev.dizyaa.dizgram.feature.user.domain.UserId
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

data class MessageSender(
    val senderId: SenderId,
)

interface SenderId

@OptIn(ExperimentalContracts::class)
fun SenderId.isUser(): Boolean {
    contract {
        returns(true) implies (this@isUser is UserId)
    }
    return this is UserId
}