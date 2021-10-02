package dev.dizyaa.dizgram.feature.auth.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.auth.data.AuthRepository
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel(
    private val repository: AuthRepository,
): StateViewModel<AuthContract.State, AuthContract.Effect>() {

    init {
        viewModelScope.launch {
            repository.getAuthStatusFlow().collect {
                Timber.d(it.toString())
                setState { copy(authStatus = it) }
                if (it == AuthStatus.Ready) {
                    setEffect { AuthContract.Effect.Navigation.ChatList }
                }
            }
        }
    }

    override fun setInitialState() = AuthContract.State.Empty

    override fun onError(exception: Exception) {

    }

    override fun onLoading(loading: Boolean) {
        setState { copy(isLoading = loading) }
    }

    fun enterByPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isNotBlank() && phoneNumber.isDigitsOnly()) {
            makeRequest {
                repository.authByPhoneNumber(phoneNumber)
            }
        }
    }

    fun loginByCode(code: String) {
        if (code.length >= 4) {
            makeRequest {
                repository.authByCode(code)
            }
        }
    }

    fun loginByPassword(password: String) {
        if (password.isNotEmpty()) {
            makeRequest {
                repository.authByPassword(password.trim())
            }
        }
    }

}

