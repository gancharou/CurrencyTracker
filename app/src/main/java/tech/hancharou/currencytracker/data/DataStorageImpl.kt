package tech.hancharou.currencytracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import tech.hancharou.currencytracker.domain.DataStorage
import tech.hancharou.currencytracker.domain.model.SortType

class DataStorageImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStorage {

    companion object {
        private val LAST_BASE_CURRENCY = stringPreferencesKey("last_base_currency")
        private val LAST_CURRENCIES_REFRESH = longPreferencesKey("last_currencies_refresh")
        private val SORT_TYPE = stringPreferencesKey("sort_type")
    }

    override suspend fun getLastBaseCurrency(): String {
        return dataStore.data.map { preferences ->
            preferences[LAST_BASE_CURRENCY] ?: "EUR"
        }.first()
    }

    override suspend fun saveBaseCurrency(currencyCode: String) {
        dataStore.edit { preferences ->
            preferences[LAST_BASE_CURRENCY] = currencyCode
        }
    }

    override suspend fun getLastCurrenciesRefreshTime(): Long {
        return dataStore.data.map { preferences ->
            preferences[LAST_CURRENCIES_REFRESH] ?: 0L
        }.first()
    }

    override suspend fun saveLastCurrenciesRefreshTime(timestamp: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_CURRENCIES_REFRESH] = timestamp
        }
    }

    override suspend fun getSortType(): SortType? {
        return dataStore.data.map { preferences ->
            preferences[SORT_TYPE]?.let { sortTypeName ->
                try {
                    SortType.valueOf(sortTypeName)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        }.first()
    }

    override suspend fun saveSortType(sortType: SortType?) {
        dataStore.edit { preferences ->
            if (sortType != null) {
                preferences[SORT_TYPE] = sortType.name
            } else {
                preferences.remove(SORT_TYPE)
            }
        }
    }
}