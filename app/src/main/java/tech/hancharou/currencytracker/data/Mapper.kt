package tech.hancharou.currencytracker.data

import tech.hancharou.currencytracker.data.nw.ExchangeRatesNW
import tech.hancharou.currencytracker.data.sw.FavoriteCurrencySW
import tech.hancharou.currencytracker.domain.model.Currency
import tech.hancharou.currencytracker.domain.model.ExchangeRate

fun Map<String, String>.toCurrencies(): List<Currency> {
    return this.map { (code, name) ->
        Currency(code = code, name = name)
    }
}

fun ExchangeRatesNW.toExchangeRates(favoritePairs: Set<Pair<String, String>>): List<ExchangeRate> {
    return this.rates.map { (quoteCurrency, rate) ->
        ExchangeRate(
            baseCurrency = this.base,
            quoteCurrency = quoteCurrency,
            rate = rate,
            isFavorite = favoritePairs.contains(this.base to quoteCurrency)
        )
    }
}

fun FavoriteCurrencySW.toExchangeRate(rate: Double): ExchangeRate {
    return ExchangeRate(
        baseCurrency = this.baseCurrency,
        quoteCurrency = this.quoteCurrency,
        rate = rate,
        isFavorite = true
    )
}