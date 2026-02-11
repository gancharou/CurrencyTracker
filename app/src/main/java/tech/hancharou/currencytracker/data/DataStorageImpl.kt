package tech.hancharou.currencytracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import tech.hancharou.currencytracker.domain.DataStorage

class DataStorageImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStorage {

    companion object {
        private val KEY_BASE_CURRENCY = stringPreferencesKey("base_currency")
        private const val DEFAULT_BASE_CURRENCY = "EUR"
    }

    override suspend fun getLastBaseCurrency(): String {
        val preferences = dataStore.data.first()
        return preferences[KEY_BASE_CURRENCY] ?: DEFAULT_BASE_CURRENCY
    }

    override suspend fun saveBaseCurrency(currencyCode: String) {
        dataStore.edit { preferences ->
            preferences[KEY_BASE_CURRENCY] = currencyCode
        }
    }
}