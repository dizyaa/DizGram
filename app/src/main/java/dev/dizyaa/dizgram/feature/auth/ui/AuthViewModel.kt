package dev.dizyaa.dizgram.feature.auth.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.auth.data.AuthRepository
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn

class AuthViewModel(
    private val repository: AuthRepository,
): StateViewModel<AuthContract.State, AuthContract.Event, AuthContract.Effect>() {

    private val authStatus = repository.authStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AuthStatus.WaitParams,
        )

    init {
        subscribeAuthStatusForUiState()

        makeRequest {
            authStatus
                .collect {
                    when (it) {
                        AuthStatus.WaitEncryptedKey -> repository.loadEncryptedKey()
                        AuthStatus.WaitParams -> repository.loadParams()
                        else -> Unit
                    }
                }
        }
    }

    override fun setInitialState() = AuthContract.State(
        isLoading = false,
        authStatus = AuthStatus.WaitParams,
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
            subscribeAuthStatusForProcess(phoneNumber)
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

    private fun subscribeAuthStatusForUiState() {
        makeRequest {
            authStatus
                .filter {
                    when (it) {
                        AuthStatus.Ready,
                        AuthStatus.WaitPhoneNumber,
                        AuthStatus.WaitCode,
                        AuthStatus.WaitOtherDeviceConfirmation,
                        AuthStatus.WaitRegistration,
                        AuthStatus.WaitPassword -> true
                        else -> false
                    }
                }
                .collect {
                    setState { copy(authStatus = it) }

                    if (it == AuthStatus.Ready) {
                        setEffect { AuthContract.Effect.Navigation.ChatList }
                    }
                }
        }
    }

    private fun subscribeAuthStatusForProcess(
        phoneNumber: String
    ) {
        makeRequest {
            authStatus
                .collect {
                    when (it) {
                        AuthStatus.WaitPhoneNumber -> repository.authByPhoneNumber(phoneNumber)
                        else -> Unit
                    }
                }
        }
    }
}

