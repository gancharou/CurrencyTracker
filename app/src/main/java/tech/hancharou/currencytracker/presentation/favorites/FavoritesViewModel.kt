package tech.hancharou.currencytracker.presentation.favorites

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tech.hancharou.currencytracker.domain.Repository
import tech.hancharou.currencytracker.presentation.favorites.mapper.toFavoritesUI
import tech.hancharou.currencytracker.presentation.favorites.model.FavoritesUI

sealed interface FavoritesViewState {
    @Immutable
    data object Loading : FavoritesViewState

    @Immutable
    data class Content(
        val data: FavoritesUI,
        val isRefreshing: Boolean = false
    ) : FavoritesViewState

    @Immutable
    data class Error(
        val message: String,
        val canRetry: Boolean = true
    ) : FavoritesViewState
}

fun initFavoritesViewState(): FavoritesViewState = FavoritesViewState.Loading

sealed interface FavoritesActions {
    data class ShowError(val message: String) : FavoritesActions
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(initFavoritesViewState())
    val state = _state.asStateFlow()

    private val _actions = Channel<FavoritesActions>()
    val actions = _actions.receiveAsFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            try {
                _state.value = FavoritesViewState.Loading
                loadFavorites()
            } catch (e: Exception) {
                handleError("Initialize", e)
            }
        }
    }

    private suspend fun loadFavorites() {
        val favoritePairs = repository.getFavoritePairs()
        val favoritesUI = favoritePairs.toFavoritesUI()

        _state.value = FavoritesViewState.Content(data = favoritesUI)
    }

    fun toggleFavorite(baseCurrency: String, quoteCurrency: String) {
        viewModelScope.launch {
            try {
                repository.removeFromFavorites(baseCurrency, quoteCurrency)
                loadFavorites()
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Toggle favorite error: ${e.message}", e)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                setRefreshing()
                loadFavorites()
            } catch (e: Exception) {
                handleError("Refresh", e)
            }
        }
    }

    fun retry() {
        initialize()
    }

    private fun setRefreshing() {
        val currentState = _state.value
        if (currentState is FavoritesViewState.Content) {
            _state.value = currentState.copy(isRefreshing = true)
        }
    }

    private fun handleError(operation: String, e: Exception) {
        Log.e("FavoritesViewModel", "$operation error: ${e.message}", e)
        _state.value = FavoritesViewState.Error(
            message = "Failed to load data"
        )
    }
}