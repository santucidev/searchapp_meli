package com.santucci.searchappdesafio.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santucci.searchappdesafio.presentation.detail.state.ProductDetailUiState
import com.santucci.searchappdesafio.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ERROR_DETAIL = "Não foi possível encontrar os detalhes do produto. Tente novamente."

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState

    fun loadProduct(itemId: String) {
        viewModelScope.launch {
            delay(1000)
            val product = repository.getProductDetails(itemId)
            if (product != null) {
                _uiState.value = ProductDetailUiState.Success(product)
            } else {
                _uiState.value =
                    ProductDetailUiState.Error(ERROR_DETAIL)
            }
        }
    }
}
