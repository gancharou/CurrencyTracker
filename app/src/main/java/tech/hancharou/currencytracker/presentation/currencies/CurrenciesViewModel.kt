package tech.hancharou.currencytracker.presentation.currencies

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
import tech.hancharou.currencytracker.presentation.currencies.mapper.buildCurrenciesUI
import tech.hancharou.currencytracker.presentation.currencies.model.CurrenciesUI

sealed interface CurrenciesViewState {
    @Immutable
    data object Loading : CurrenciesViewState

    @Immutable
    data class Content(
        val data: CurrenciesUI,
        val isRefreshing: Boolean = false
    ) : CurrenciesViewState

    @Immutable
    data class Error(
        val message: String,
        val canRetry: Boolean = true
    ) : CurrenciesViewState
}

fun initCurrenciesViewState(): CurrenciesViewState = CurrenciesViewState.Loading

sealed interface CurrenciesActions {
    data object NavigateToFilters : CurrenciesActions
    data class ShowError(val message: String) : CurrenciesActions
}

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(initCurrenciesViewState())
    val state = _state.asStateFlow()

    private val _actions = Channel<CurrenciesActions>()
    val actions = _actions.receiveAsFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            try {
                _state.value = CurrenciesViewState.Loading
                loadCurrencies()
            } catch (e: Exception) {
                handleError("Initialize", e)
            }
        }
    }

    private suspend fun loadCurrencies() {
        val baseCurrency = repository.getLastBaseCurrency()
        val currencies = repository.getCurrencies()
        val exchangeRates = repository.getExchangeRates(baseCurrency)

        val currenciesUI = buildCurrenciesUI(
            baseCurrency = baseCurrency,
            currencies = currencies,
            exchangeRates = exchangeRates
        )

        _state.value = CurrenciesViewState.Content(data = currenciesUI)
    }

    fun selectBaseCurrency(currencyCode: String) {
        viewModelScope.launch {
            try {
                repository.saveBaseCurrency(currencyCode)
                setRefreshing()
                loadCurrencies()
            } catch (e: Exception) {
                handleError("Select currency", e)
            }
        }
    }

    fun toggleFavorite(baseCurrency: String, quoteCurrency: String) {
        viewModelScope.launch {
            try {
                val currentState = _state.value
                if (currentState !is CurrenciesViewState.Content) return@launch

                val item = currentState.data.exchangeRates.find {
                    it.currencyCode == quoteCurrency
                }

                if (item?.isFavorite == true) {
                    repository.removeFromFavorites(baseCurrency, quoteCurrency)
                } else {
                    repository.addToFavorites(baseCurrency, quoteCurrency)
                }

                loadCurrencies()
            } catch (e: Exception) {
                Log.e("CurrenciesViewModel", "Toggle favorite error: ${e.message}", e)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                setRefreshing()
                loadCurrencies()
            } catch (e: Exception) {
                handleError("Refresh", e)
            }
        }
    }

    fun retry() {
        initialize()
    }

    fun onFilterClick() {
        _actions.trySend(CurrenciesActions.NavigateToFilters)
    }

    private fun setRefreshing() {
        val currentState = _state.value
        if (currentState is CurrenciesViewState.Content) {
            _state.value = currentState.copy(isRefreshing = true)
        }
    }

    private fun handleError(operation: String, e: Exception) {
        Log.e("CurrenciesViewModel", "$operation error: ${e.message}", e)
        _state.value = CurrenciesViewState.Error(
            message = "Failed to load data"
        )
    }
}