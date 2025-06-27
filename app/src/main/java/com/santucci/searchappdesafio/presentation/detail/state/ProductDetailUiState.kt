package com.santucci.searchappdesafio.presentation.detail.state

import com.santucci.searchappdesafio.data.model.ItemDetail

sealed interface ProductDetailUiState {
     object Loading : ProductDetailUiState
    data class Success(val product: ItemDetail) : ProductDetailUiState
    data class Error(val message: String) : ProductDetailUiState
}
