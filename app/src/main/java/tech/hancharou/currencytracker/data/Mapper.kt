package tech.hancharou.currencytracker.data

import tech.hancharou.currencytracker.data.nw.ExchangeRatesNW
import tech.hancharou.currencytracker.domain.model.Currency
import tech.hancharou.currencytracker.domain.model.ExchangeRate

fun Map<String, String>.toCurrencies(): List<Currency> {
    return this.map { (code, name) ->
        Currency(code = code, name = name)
    }
}

fun ExchangeRatesNW.toExchangeRates(favoriteCodes: Set<String>): List<ExchangeRate> {
    return this.rates.map { (code, rate) ->
        ExchangeRate(
            currencyCode = code,
            rate = rate,
            isFavorite = favoriteCodes.contains(code)
        )
    }
}