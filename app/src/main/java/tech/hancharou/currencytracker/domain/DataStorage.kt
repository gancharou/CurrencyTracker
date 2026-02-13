package tech.hancharou.currencytracker.domain

import tech.hancharou.currencytracker.domain.model.SortType

interface DataStorage {
    suspend fun getLastBaseCurrency(): String
    suspend fun saveBaseCurrency(currencyCode: String)

    suspend fun getLastCurrenciesRefreshTime(): Long
    suspend fun saveLastCurrenciesRefreshTime(timestamp: Long)

    suspend fun getSortType(): SortType?
    suspend fun saveSortType(sortType: SortType?)
}