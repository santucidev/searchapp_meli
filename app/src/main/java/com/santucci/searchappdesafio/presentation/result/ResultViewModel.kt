package com.santucci.searchappdesafio.presentation.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santucci.searchappdesafio.data.model.SearchResult
import com.santucci.searchappdesafio.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<SearchResult>>(emptyList())
    val products: StateFlow<List<SearchResult>> = _products

    // Logs adicionado ->  Gestão de casos de erros do ponto de vista do desenvolvedor.

    fun search(query: String) {
        viewModelScope.launch {
            Log.d("DEBUG", "ResultViewModel: buscando por '$query'")
            val response = repository.searchProducts(query)
            _products.value = response.results
            Log.d("DEBUG", "ResultViewModel: encontrados ${_products.value.size} produtos.")
        }
    }
}
