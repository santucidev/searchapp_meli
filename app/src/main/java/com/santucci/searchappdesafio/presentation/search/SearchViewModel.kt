package com.santucci.searchappdesafio.presentation.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santucci.searchappdesafio.R
import com.santucci.searchappdesafio.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    @StringRes val errorMessageRes: Int? = null,
    val errorQuery: String? = null,
    val searchText: String = ""
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private fun onQueryChanged() {
        _uiState.value = _uiState.value.copy(errorMessageRes = null, errorQuery = null)
    }

    fun onSearchTextChanged(newText: String) {
        _uiState.value = _uiState.value.copy(searchText = newText)
        if (_uiState.value.errorMessageRes != null) {
            onQueryChanged()
        }
    }

    fun resetState() {
        _uiState.value = SearchUiState()
    }

    fun onSearchClicked(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessageRes = R.string.search_error_empty)
            return
        }

        if (repository.isValidQuery(query)) {
            viewModelScope.launch {
                _navigationEvent.emit(query)
            }
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessageRes = R.string.search_error_not_found,
                errorQuery = query
            )
        }
    }
}
