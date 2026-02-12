package tech.hancharou.currencytracker.presentation.main


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface MainActions {
    data object OnCurrenciesClick : MainActions
    data object OnFavoritesClick : MainActions
}

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _actions = Channel<MainActions>()
    val actions = _actions.receiveAsFlow()

    fun goToCurrencies() {
        _actions.trySend(MainActions.OnCurrenciesClick)
    }

    fun goToFavorites() {
        _actions.trySend(MainActions.OnFavoritesClick)
    }
}