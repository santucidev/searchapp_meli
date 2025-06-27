package com.santucci.searchappdesafio.presentation.search

import com.santucci.searchappdesafio.R
import com.santucci.searchappdesafio.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private lateinit var mockRepository: ProductRepository

    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        viewModel = SearchViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchClicked WHEN query is invalid MUST update uiState with error message`() {
        val query = "query invalida"
        coEvery { mockRepository.isValidQuery(query) } returns false

        viewModel.onSearchClicked(query)

        val currentState = viewModel.uiState.value
        assertEquals(R.string.search_error_not_found, currentState.errorMessageRes)
        assertEquals(query, currentState.errorQuery)
    }

    @Test
    fun `onSearchClicked WHEN query is blank MUST update uiState with empty field error`() {
        val query = ""

        viewModel.onSearchClicked(query)

        val currentState = viewModel.uiState.value
        assertEquals(R.string.search_error_empty, currentState.errorMessageRes)
    }
}