package dev.dizyaa.dizgram.feature.auth.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.auth.data.AuthRepository
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AuthViewModel(
    private val repository: AuthRepository,
): StateViewModel<AuthContract.State, AuthContract.Event, AuthContract.Effect>() {

    init {
        handleAuthStatus()
        makeRequest {
            processAuthStatus(repository.getAuthStatus())
        }
    }

    override fun setInitialState() = AuthContract.State(
        isLoading = false,
        authStatus = AuthStatus.Closed,
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
                if (repository.getAuthStatus() == AuthStatus.WaitPhoneNumber) {
                    repository.authByPhoneNumber(phoneNumber)
                }
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

    private fun handleAuthStatus() {
        makeRequest {
            repository.authStatus
                .onEach { processAuthStatus(it) }
                .launchIn(viewModelScope)
        }
    }

    private fun processAuthStatus(authStatus: AuthStatus) {
        when (authStatus) {
            AuthStatus.Ready,
            AuthStatus.WaitPhoneNumber,
            AuthStatus.WaitCode,
            AuthStatus.WaitOtherDeviceConfirmation,
            AuthStatus.WaitRegistration,
            AuthStatus.WaitPassword -> setAuthStatus(authStatus)
            AuthStatus.WaitEncryptedKey -> loadEncryptedKey()
            AuthStatus.WaitParams -> loadParams()
            AuthStatus.Closed,
            AuthStatus.Closing,
            AuthStatus.LoggingOut -> Unit
        }
    }

    private fun loadEncryptedKey() {
        makeRequest {
            repository.loadEncryptedKey()
        }
    }

    private fun loadParams() {
        makeRequest {
            repository.loadParams()
        }
    }

    private fun setAuthStatus(authStatus: AuthStatus) {
        setState { copy(authStatus = authStatus) }

        if (authStatus == AuthStatus.Ready) {
            setEffect { AuthContract.Effect.Navigation.ChatList }
        }
    }
}

