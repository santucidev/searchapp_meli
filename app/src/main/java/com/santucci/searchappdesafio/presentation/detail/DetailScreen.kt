package com.santucci.searchappdesafio.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santucci.searchappdesafio.R
import com.santucci.searchappdesafio.data.model.ItemDetail
import com.santucci.searchappdesafio.presentation.detail.state.ProductDetailUiState
import com.santucci.searchappdesafio.ui.theme.Spacing_0DP
import com.santucci.searchappdesafio.ui.theme.Spacing_16DP
import com.santucci.searchappdesafio.ui.theme.Spacing_2DP
import com.santucci.searchappdesafio.ui.theme.Spacing_32DP
import com.santucci.searchappdesafio.ui.theme.Spacing_350DP
import com.santucci.searchappdesafio.ui.theme.Spacing_50DP
import com.santucci.searchappdesafio.ui.theme.Spacing_64DP
import com.santucci.searchappdesafio.ui.theme.Spacing_8DP
import com.santucci.searchappdesafio.ui.theme.TextHugeLarge
import com.santucci.searchappdesafio.util.CurrencyUtils.formatCurrency

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ProductDetailUiState.Loading -> {
            LoadingState()
        }

        is ProductDetailUiState.Success -> {
            ProductDetailContent(product = state.product)
        }

        is ProductDetailUiState.Error -> {
            ErrorState(message = state.message, onRetry = {
                navController.popBackStack()
            })
        }
    }
}

@Composable
fun ProductDetailContent(
    product: ItemDetail
) {
    val sep = stringResource(R.string.category_separator)

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            val pagerState = rememberPagerState { product.pictures.size }

            Box(contentAlignment = Alignment.BottomCenter) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Spacing_350DP)
                ) { page ->
                    AsyncImage(
                        model = product.pictures[page].url.replace(
                            "http://", "https://"),
                        contentDescription = product.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Row(
                    Modifier
                        .height(Spacing_50DP)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.2f))
                        .padding(horizontal = Spacing_8DP),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration)
                                MaterialTheme.colorScheme.primary
                            else Color.LightGray.copy(
                                alpha = 0.5f
                            )
                        Box(
                            modifier = Modifier
                                .padding(Spacing_2DP)
                                .clip(CircleShape)
                                .background(color)
                                .size(Spacing_8DP)
                        )
                    }
                }
            }
        }
        item {
            Column(
                modifier = Modifier.padding(Spacing_16DP)
            ) {
                Text(
                    text = product.categoryPath.joinToString(sep) { it.name },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(Spacing_8DP))

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(Spacing_8DP))

                Text(
                    text = formatCurrency(product.price, product.currencyId),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(Spacing_16DP))

                Text(
                    stringResource(R.string.detail_description),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = Spacing_8DP)
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = TextHugeLarge
                )

                Spacer(modifier = Modifier.height(Spacing_32DP))

                Text(
                    stringResource(R.string.detail_attributes),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = Spacing_8DP))

                product.attributes
                    .filter { !it.valueName.isNullOrBlank() }
                    .forEach { attr ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing_8DP),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${attr.name}:",
                                modifier = Modifier.weight(0.4f),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = attr.valueName!!,
                                modifier = Modifier.weight(0.6f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    }
            }
        }
    }
}

@Composable
fun LoadingState(
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(Spacing_8DP))
            Text(stringResource(R.string.loading_details))
        }
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing_16DP),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(Spacing_64DP),
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(Spacing_16DP))

            Text(
                text = message,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(Spacing_16DP))

            Button(
                onClick = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing_50DP),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background,
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = Spacing_8DP,
                    pressedElevation = Spacing_2DP,
                    disabledElevation = Spacing_0DP
                )
            ) {
                Text(stringResource(R.string.detail_error_button_back))
            }
        }
    }
}
