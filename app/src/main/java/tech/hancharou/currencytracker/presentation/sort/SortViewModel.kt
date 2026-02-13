package tech.hancharou.currencytracker.presentation.sort

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
import tech.hancharou.currencytracker.domain.DataStorage
import tech.hancharou.currencytracker.domain.model.SortType

@Immutable
data class SortViewState(
    val selectedSort: SortType?
)

sealed interface SortActions {
    data object NavigateBack : SortActions
}

@HiltViewModel
class SortViewModel @Inject constructor(
    private val dataStorage: DataStorage
) : ViewModel() {

    private val _state = MutableStateFlow(SortViewState(selectedSort = null))
    val state = _state.asStateFlow()

    private val _actions = Channel<SortActions>()
    val actions = _actions.receiveAsFlow()

    init {
        loadCurrentSort()
    }

    private fun loadCurrentSort() {
        viewModelScope.launch {
            val currentSort = dataStorage.getSortType()
            _state.value = SortViewState(selectedSort = currentSort)
        }
    }

    fun selectSort(sortType: SortType?) {
        _state.value = _state.value.copy(selectedSort = sortType)
    }

    fun apply() {
        viewModelScope.launch {
            dataStorage.saveSortType(_state.value.selectedSort)
            _actions.send(SortActions.NavigateBack)
        }
    }

    fun cancel() {
        _actions.trySend(SortActions.NavigateBack)
    }
}