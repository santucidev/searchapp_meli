package com.santucci.searchappdesafio.presentation.result

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.santucci.searchappdesafio.R
import com.santucci.searchappdesafio.data.model.SearchResult
import com.santucci.searchappdesafio.ui.theme.Spacing_150DP
import com.santucci.searchappdesafio.ui.theme.Spacing_16DP
import com.santucci.searchappdesafio.ui.theme.Spacing_2DP
import com.santucci.searchappdesafio.ui.theme.Spacing_4DP
import com.santucci.searchappdesafio.ui.theme.Spacing_8DP
import com.santucci.searchappdesafio.util.CurrencyUtils.formatCurrency

@Composable
fun ResultsScreen(
    viewModel: ResultViewModel,
    onProductClick: (String) -> Unit
) {
    val products by viewModel.products.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing_8DP),
        verticalArrangement = Arrangement.spacedBy(Spacing_8DP)
    ) {
        items(products) { product ->
            ProductItem(product = product, onClick = onProductClick)
        }
    }
}

@Composable
fun ProductItem(
    product: SearchResult,
    onClick: (String) -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_4DP)
            .clickable { onClick(product.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = Spacing_2DP),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(Spacing_16DP)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.thumbnail.replace("http://", "https://"),
                contentDescription = product.title,
                modifier = Modifier
                    .size(Spacing_150DP)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(Spacing_16DP))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(Spacing_4DP)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Column {
                    if (product.originalPrice != null && product.originalPrice > product.price) {
                        Text(
                            text = formatCurrency(product.originalPrice, product.currencyId),
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = formatCurrency(product.price, product.currencyId),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    product.installments?.let {
                        Text(
                            text = stringResource(
                                R.string.result_installments_info,
                                it.quantity,
                                formatCurrency(it.amount, product.currencyId)
                            ),
                            color = colorResource(R.color.green),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (product.shipping?.freeShipping == true) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = colorResource(R.color.green),
                            modifier = Modifier.size(Spacing_16DP)
                        )
                        Spacer(modifier = Modifier.width(Spacing_4DP))
                        Text(
                            text = stringResource(R.string.result_free_shipping),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.green)
                        )
                    }
                }
            }
        }
    }
}
