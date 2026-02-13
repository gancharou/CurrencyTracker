package tech.hancharou.currencytracker.presentation.favorites.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import tech.hancharou.currencytracker.presentation.components.CurrencyItem
import tech.hancharou.currencytracker.presentation.components.ScreenHeader
import tech.hancharou.currencytracker.presentation.favorites.FavoritesViewModel
import tech.hancharou.currencytracker.presentation.favorites.FavoritesViewState
import tech.hancharou.currencytracker.presentation.favorites.model.FavoritesUI
import tech.hancharou.currencytracker.presentation.theme.BackgroundWhite
import tech.hancharou.currencytracker.presentation.theme.PrimaryBlue
import tech.hancharou.currencytracker.presentation.theme.TextSecondary

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is FavoritesViewState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is FavoritesViewState.Content -> {
            val content = state as FavoritesViewState.Content
            FavoritesContent(
                data = content.data,
                isRefreshing = content.isRefreshing,
                onFavoriteToggle = viewModel::toggleFavorite,
                onRefresh = viewModel::refresh
            )
        }

        is FavoritesViewState.Error -> {
            val error = state as FavoritesViewState.Error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Failed to load data",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )

                    if (error.canRetry) {
                        Button(
                            onClick = { viewModel.retry() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryBlue
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesContent(
    data: FavoritesUI,
    isRefreshing: Boolean,
    onFavoriteToggle: (String, String) -> Unit,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
        ) {
            ScreenHeader(title = "Favorites")
            if (data.favorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No favorites yet\nAdd currencies to favorites",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    items(
                        items = data.favorites,
                        key = { "${it.baseCurrency}_${it.quoteCurrency}" }
                    ) { item ->
                        FavoriteItem(
                            item = item,
                            onFavoriteToggle = onFavoriteToggle
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteItem(
    item: FavoritesUI.FavoriteItem,
    onFavoriteToggle: (String, String) -> Unit
) {
    val onToggle = remember(item.baseCurrency, item.quoteCurrency, onFavoriteToggle) {
        { onFavoriteToggle(item.baseCurrency, item.quoteCurrency) }
    }

    CurrencyItem(
        currencyText = item.currencyPair,
        rate = item.rate,
        isFavorite = item.isFavorite,
        onFavoriteToggle = onToggle
    )
}