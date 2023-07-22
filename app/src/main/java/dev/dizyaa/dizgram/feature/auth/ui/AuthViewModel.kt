package dev.dizyaa.dizgram.feature.auth.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.auth.data.AuthRepository
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
): StateViewModel<AuthContract.State, AuthContract.Event, AuthContract.Effect>() {

    init {
        viewModelScope.launch {
            repository.authStatus.collect {
                setState { copy(authStatus = it) }

                if (it == AuthStatus.Ready) {
                    setEffect { AuthContract.Effect.Navigation.ChatList }
                }
            }
        }
    }

    override fun setInitialState() = AuthContract.State(
        isLoading = false,
        authStatus = AuthStatus.WaitPhoneNumber,
    )

    override fun handleEvents(event: AuthContract.Event) {
        when (event) {
            is AuthContract.Event.LoginByPassword -> loginByPassword(event.password)
            is AuthContract.Event.LoginByCode -> loginByCode(event.code)
            is AuthContract.Event.EnterByPhoneNumber -> enterByPhoneNumber(event.phoneNumber)
        }
    }

    override fun onError(exception: Exception) {

    }

    override fun onLoading(loading: Boolean) {
        setState { copy(isLoading = loading) }
    }

    private fun enterByPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isNotBlank() && phoneNumber.isDigitsOnly()) {
            makeRequest {
                repository.authByPhoneNumber(phoneNumber)
            }
        }
    }

    private fun loginByCode(code: String) {
        if (code.length >= 4) {
            makeRequest {
                repository.authByCode(code)
            }
        }
    }

    private fun loginByPassword(password: String) {
        if (password.isNotEmpty()) {
            makeRequest {
                repository.authByPassword(password.trim())
            }
        }
    }
}

