package tech.hancharou.currencytracker.presentation.currencies.mapper

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlinx.collections.immutable.toImmutableList
import tech.hancharou.currencytracker.domain.model.Currency
import tech.hancharou.currencytracker.domain.model.ExchangeRate
import tech.hancharou.currencytracker.presentation.currencies.model.CurrenciesUI

fun List<Currency>.toAvailableCurrencies(): List<String> {
    return this.map { it.code }
}

fun List<ExchangeRate>.toCurrencyItems(): List<CurrenciesUI.CurrencyItem> {
    return this.map { exchangeRate ->
        CurrenciesUI.CurrencyItem(
            currencyCode = exchangeRate.quoteCurrency,
            rate = exchangeRate.rate.formatRate(),
            isFavorite = exchangeRate.isFavorite
        )
    }
}

fun buildCurrenciesUI(
    baseCurrency: String,
    currencies: List<Currency>,
    exchangeRates: List<ExchangeRate>
): CurrenciesUI {
    return CurrenciesUI(
        baseCurrency = baseCurrency,
        availableCurrencies = currencies.toAvailableCurrencies().toImmutableList(),
        exchangeRates = exchangeRates.toCurrencyItems().toImmutableList()
    )
}

private fun Double.formatRate(): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        decimalSeparator = '.'
    }
    val formatter = DecimalFormat("#.######", symbols)
    return formatter.format(this)
}