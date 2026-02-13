package tech.hancharou.currencytracker.presentation.favorites.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class FavoritesUI(
    val favorites: ImmutableList<FavoriteItem>
) {
    @Immutable
    data class FavoriteItem(
        val baseCurrency: String,     // "EUR"
        val quoteCurrency: String,    // "AED"
        val currencyPair: String,     // "EUR/AED"
        val rate: String,             // "3.932455"
        val isFavorite: Boolean = true
    )
}