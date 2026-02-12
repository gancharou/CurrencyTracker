package tech.hancharou.currencytracker.domain

interface DataStorage {
    suspend fun getLastBaseCurrency(): String
    suspend fun saveBaseCurrency(currencyCode: String)

    suspend fun getLastCurrenciesRefreshTime(): Long
    suspend fun saveLastCurrenciesRefreshTime(timestamp: Long)
}