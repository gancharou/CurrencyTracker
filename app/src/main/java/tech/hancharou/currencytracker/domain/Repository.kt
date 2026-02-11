package tech.hancharou.currencytracker.domain

import tech.hancharou.currencytracker.domain.model.Currency
import tech.hancharou.currencytracker.domain.model.ExchangeRate

interface Repository {

    suspend fun getCurrencies(): List<Currency>

    suspend fun getExchangeRates(baseCurrency: String): List<ExchangeRate>

    suspend fun getFavoriteCurrencies(): List<String>

    suspend fun addToFavorites(currencyCode: String)

    suspend fun removeFromFavorites(currencyCode: String)

    suspend fun getLastBaseCurrency(): String

    suspend fun saveBaseCurrency(currencyCode: String)
}