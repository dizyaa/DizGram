package dev.dizyaa.dizgram.core.uihelpers

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext


@Stable
interface UiState {
    val isLoading: Boolean
}

interface UiEffect

interface UiEvent

const val SIDE_EFFECTS_KEY = "side-effects_key"

abstract class StateViewModel<
        State : UiState,
        Event : UiEvent,
        Effect : UiEffect
        >: ViewModel() {

    abstract fun setInitialState(): State

    private val initialState: State by lazy { setInitialState() }

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)

    val state: StateFlow<State> = _state

    private val _effect: Channel<Effect> = Channel()

    val effect: Flow<Effect> = _effect.receiveAsFlow()
    private val loadingFlags: MutableSet<Int> = mutableSetOf()


    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    protected abstract fun handleEvents(event: Event)


    init {
        subscribeToEvents()
    }


    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

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
        context: CoroutineContext = Dispatchers.IO,
        request: suspend () -> (T),
    ) {
        val flag = request.hashCode()

        viewModelScope.launch {
            withContext(context) {
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

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvents(it)
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