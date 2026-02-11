package tech.hancharou.currencytracker.domain

interface DataStorage {
    suspend fun getLastBaseCurrency(): String
    suspend fun saveBaseCurrency(currencyCode: String)
}