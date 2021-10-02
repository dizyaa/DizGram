package dev.dizyaa.dizgram.core.uihelpers

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException


@Stable
interface UiState {
    val isLoading: Boolean
}

interface UiEffect

const val SIDE_EFFECTS_KEY = "side-effects_key"

abstract class StateViewModel<
        State : UiState,
        Effect: UiEffect
        >: ViewModel() {

    protected val gson: Gson = GsonBuilder().create()

    abstract fun setInitialState(): State

    private val initialState: State by lazy { setInitialState() }

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)

    val state: StateFlow<State> = _state

    private val _effect: Channel<Effect> = Channel()

    val effect: Flow<Effect> = _effect.receiveAsFlow()
    private val loadingFlags: MutableSet<Int> = mutableSetOf()

    protected fun setState(reducer: State.() -> State) {
        val newState = _state.value.reducer()
        _state.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    abstract fun onError(exception: Exception)
    abstract fun onLoading(loading: Boolean)

    protected fun <T> makeRequest(
        onLoadingChange: (Boolean) -> Unit = {},
        onFailure: (Exception) -> Unit = { onError(it) },
        handleNetworkFailure: Boolean = false,
        withIndication: Boolean = true,
        request: suspend () -> (T),
    ) {
        val flag = request.hashCode()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (withIndication) addLoadingFlag(flag)
                    onLoadingChange(true)
                    request()
                } catch (ex: Exception) {
                    if (handleNetworkFailure || ex !is UnknownHostException) {
                        onFailure(ex)
                    }
                } finally {
                    if (withIndication) removeLoadingFlag(flag)
                    onLoadingChange(false)
                }
            }
        }
    }

    private fun addLoadingFlag(flag: Int) {
        loadingFlags.add(flag)
        onLoading(true)
    }

    private fun removeLoadingFlag(flag: Int) {
        loadingFlags.remove(flag)
        if (loadingFlags.isEmpty()) onLoading(false)
    }
}