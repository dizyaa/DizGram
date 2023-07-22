package dev.dizyaa.dizgram.feature.auth.ui

import dev.dizyaa.dizgram.core.uihelpers.UiEffect
import dev.dizyaa.dizgram.core.uihelpers.UiEvent
import dev.dizyaa.dizgram.core.uihelpers.UiState
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus

class AuthContract {
    data class State(
        override val isLoading: Boolean,
        val authStatus: AuthStatus,
    ): UiState

    sealed class Event : UiEvent {
        data class EnterByPhoneNumber(val phoneNumber: String) : Event()
        data class LoginByCode(val code: String) : Event()
        data class LoginByPassword(val password: String) : Event()
    }

    sealed class Effect : UiEffect {

        sealed class Navigation : Effect() {
            object ChatList : Navigation()
        }
    }
}