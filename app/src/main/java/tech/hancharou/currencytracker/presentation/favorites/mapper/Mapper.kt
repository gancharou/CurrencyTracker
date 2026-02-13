package tech.hancharou.currencytracker.presentation.favorites.mapper

import kotlinx.collections.immutable.toImmutableList
import tech.hancharou.currencytracker.domain.model.ExchangeRate
import tech.hancharou.currencytracker.presentation.favorites.model.FavoritesUI
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun List<ExchangeRate>.toFavoritesUI(): FavoritesUI {
    val items = this.map { exchangeRate ->
        FavoritesUI.FavoriteItem(
            baseCurrency = exchangeRate.baseCurrency,
            quoteCurrency = exchangeRate.quoteCurrency,
            currencyPair = "${exchangeRate.baseCurrency}/${exchangeRate.quoteCurrency}",
            rate = exchangeRate.rate.formatRate(),
            isFavorite = true
        )
    }

    return FavoritesUI(favorites = items.toImmutableList())
}

private fun Double.formatRate(): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        decimalSeparator = '.'
    }
    val formatter = DecimalFormat("#.######", symbols)
    return formatter.format(this)
}