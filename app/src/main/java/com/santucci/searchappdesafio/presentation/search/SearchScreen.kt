package com.santucci.searchappdesafio.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.santucci.searchappdesafio.R
import com.santucci.searchappdesafio.ui.theme.Spacing_0DP
import com.santucci.searchappdesafio.ui.theme.Spacing_16DP
import com.santucci.searchappdesafio.ui.theme.Spacing_2DP
import com.santucci.searchappdesafio.ui.theme.Spacing_4DP
import com.santucci.searchappdesafio.ui.theme.Spacing_50DP
import com.santucci.searchappdesafio.ui.theme.Spacing_8DP
import com.santucci.searchappdesafio.ui.theme.TextHugeSize

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.resetState()
        viewModel.navigationEvent.collect { query ->
            navController.navigate("results/$query")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing_16DP)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = stringResource(id = R.string.search_screen_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(Spacing_16DP))

        OutlinedTextField(
            value = uiState.searchText,
            onValueChange = {
                viewModel.onSearchTextChanged(it)
            },
            placeholder = {
                Text(
                    stringResource(id = R.string.search_screen_field),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (uiState.searchText.isNotBlank()) {
                    IconButton(
                        onClick = {
                            viewModel.onSearchTextChanged("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            },
            textStyle = TextStyle(
                fontSize = TextHugeSize,
                fontWeight = FontWeight.Medium),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorMessageRes != null,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                viewModel.onSearchClicked(uiState.searchText)
            }),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(Spacing_16DP))

        ErrorMessage(uiState = uiState)

        SuggestionChips(
            onSuggestionClick = { suggestion ->
                viewModel.onSearchTextChanged(suggestion)
                keyboardController?.hide()
                viewModel.onSearchClicked(suggestion)
            }
        )

        Spacer(modifier = Modifier.height(Spacing_16DP))

        Button(
            onClick = {
                viewModel.onSearchClicked(uiState.searchText)
                keyboardController?.hide()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing_50DP),
            enabled = uiState.searchText.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background,
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = Spacing_8DP,
                pressedElevation = Spacing_2DP,
                disabledElevation = Spacing_0DP
            )
        )
        {
            Text(
                text = stringResource(id = R.string.search_button),
                fontSize = TextHugeSize,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ErrorMessage(uiState: SearchUiState) {
    uiState.errorMessageRes?.let { errorResId ->
        val message = if (uiState.errorQuery != null) {
            stringResource(id = errorResId, uiState.errorQuery)
        } else {
            stringResource(id = errorResId)
        }

        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(Spacing_16DP))

    }
}

@Composable
fun SuggestionChips(
    onSuggestionClick: (String) -> Unit
) {
    val productSearchSuggestions = listOf(
        stringResource(R.string.search_suggestion_zapatilla),
        stringResource(R.string.search_suggestion_iphone),
        stringResource(R.string.search_suggestion_arroz),
        stringResource(R.string.search_suggestion_cafe),
        stringResource(R.string.search_suggestion_camisa),
        stringResource(R.string.search_suggestion_bermuda)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.search_suggestions_products),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(Spacing_8DP))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(Spacing_8DP)
        ) {
            productSearchSuggestions.forEach { suggestion ->
                AssistChip(
                    modifier = Modifier.padding(horizontal = Spacing_4DP),
                    onClick = { onSuggestionClick(suggestion) },
                    label = {
                        Text(
                            suggestion,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        leadingIconContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}
