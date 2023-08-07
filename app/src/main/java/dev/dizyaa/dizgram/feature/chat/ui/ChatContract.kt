package dev.dizyaa.dizgram.feature.chat.ui

import dev.dizyaa.dizgram.core.uihelpers.UiEffect
import dev.dizyaa.dizgram.core.uihelpers.UiEvent
import dev.dizyaa.dizgram.core.uihelpers.UiState

class ChatContract {

    data class State(
        override val isLoading: Boolean
    ): UiState

    sealed class Event: UiEvent {

    }

    sealed class Effect: UiEffect {

        sealed class Navigation: Effect()
    }
}