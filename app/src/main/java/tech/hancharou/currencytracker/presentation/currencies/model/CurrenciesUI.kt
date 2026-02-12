package tech.hancharou.currencytracker.presentation.currencies.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class CurrenciesUI(
    val baseCurrency: String,                        // "EUR" - выбранная базовая
    val availableCurrencies: ImmutableList<String>,  // все доступные для дропдауна
    val exchangeRates: ImmutableList<CurrencyItem>   // список курсов
) {
    @Immutable
    data class CurrencyItem(
        val currencyCode: String,  // "USD"
        val rate: String,          // "2.768594"
        val isFavorite: Boolean    // true/false
    )
}