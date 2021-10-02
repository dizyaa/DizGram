package dev.dizyaa.dizgram.feature.auth.ui

import dev.dizyaa.dizgram.core.uihelpers.UiEffect
import dev.dizyaa.dizgram.core.uihelpers.UiState
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus

class AuthContract {
    data class State(
        override val isLoading: Boolean,
        val authStatus: AuthStatus,
    ): UiState {
        companion object {
            val Empty = State(
                isLoading = false,
                authStatus = AuthStatus.WaitPhoneNumber,
            )
        }
    }

    sealed class Effect : UiEffect {

        sealed class Navigation : Effect() {
            object ChatList : Navigation()
        }
    }
}