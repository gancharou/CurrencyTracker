package tech.hancharou.currencytracker.data

import tech.hancharou.currencytracker.data.nw.ExchangeRatesNW
import tech.hancharou.currencytracker.data.sw.CurrencySW
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

fun CurrencySW.toCurrency(): Currency {
    return Currency(
        code = this.code,
        name = this.name
    )
}

fun Map<String, String>.toCurrenciesSW(): List<CurrencySW> {
    return this.map { (code, name) ->
        CurrencySW(code = code, name = name)
    }
}

fun createFavoriteCurrencySW(baseCurrency: String, quoteCurrency: String): FavoriteCurrencySW {
    return FavoriteCurrencySW(
        baseCurrency = baseCurrency,
        quoteCurrency = quoteCurrency
    )
}