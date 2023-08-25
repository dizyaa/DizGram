package dev.dizyaa.dizgram.feature.chat.ui

import androidx.lifecycle.viewModelScope
import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.chat.data.ChatRepository
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard
import dev.dizyaa.dizgram.feature.user.data.UserRepository
import dev.dizyaa.dizgram.feature.user.domain.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) : StateViewModel<ChatContract.State, ChatContract.Event, ChatContract.Effect>() {

    private val currentUser = CompletableDeferred<User>()

    init {
        initUser()
        subscribeMessages()
    }

    override fun handleEvents(event: ChatContract.Event) {
        when (event) {
            else -> Unit
        }
    }

    override fun onLoading(loading: Boolean) {
        setState { copy(isLoading = loading) }
    }

    override fun onError(exception: Exception) = Unit

    override fun setInitialState() = ChatContract.State(
        isLoading = false,
        chatImage = null,
        chatTitle = "Loading...",
        messages = emptyList(),
    )

    private fun Message.toUi(user: User): MessageCard {
        return MessageCard(
            id = this.id,
            senderId = this.sender.senderId,
            contentText = this.content,
            contentLink = null,
            contentImages = emptyList(),
            sendingStatus = this.status,
            authorName = null,
            fromMe = user.userId == this.sender.senderId
        )
    }

    private fun subscribeMessages() {
        chatRepository.messages
            .onEach {
                val user = currentUser.await()
                setState { copy(messages = messages + it.toUi(user)) }

                Timber.d(it.toString())
            }
            .launchIn(viewModelScope)
    }

    private fun initUser() {
        makeRequest {
            currentUser.complete(userRepository.getCurrentUser())
        }
    }
}
