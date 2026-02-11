package tech.hancharou.currencytracker.domain

import tech.hancharou.currencytracker.domain.model.Currency
import tech.hancharou.currencytracker.domain.model.ExchangeRate

interface Repository {

    suspend fun getCurrencies(): List<Currency>

    suspend fun getExchangeRates(baseCurrency: String): List<ExchangeRate>

    suspend fun getFavoritePairs(): List<ExchangeRate>

    suspend fun addToFavorites(baseCurrency: String, quoteCurrency: String)

    suspend fun removeFromFavorites(baseCurrency: String, quoteCurrency: String)

    suspend fun isFavorite(baseCurrency: String, quoteCurrency: String): Boolean

    suspend fun getLastBaseCurrency(): String

    suspend fun saveBaseCurrency(currencyCode: String)
}