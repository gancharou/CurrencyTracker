package tech.hancharou.currencytracker.data

import javax.inject.Inject
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tech.hancharou.currencytracker.data.nw.ApiService
import tech.hancharou.currencytracker.data.sw.AppDatabase
import tech.hancharou.currencytracker.domain.DataStorage
import tech.hancharou.currencytracker.domain.Repository
import tech.hancharou.currencytracker.domain.model.Currency
import tech.hancharou.currencytracker.domain.model.ExchangeRate

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataStorage: DataStorage,
    database: AppDatabase
) : Repository {

    private val favoriteCurrencyDao = database.favoriteCurrencyDao()
    private val currencyDao = database.currencyDao()
    private val refreshMutex = Mutex()

    override suspend fun getCurrencies(): List<Currency> {
        val cached = currencyDao.getAllCurrencies()

        if (cached.isEmpty()) {
            refreshCurrencies()
            return currencyDao.getAllCurrencies().map { it.toCurrency() }
        }

        return cached.map { it.toCurrency() }
    }

    override suspend fun refreshCurrencies() {
        refreshMutex.withLock {
            val now = System.currentTimeMillis()
            val lastRefresh = dataStorage.getLastCurrenciesRefreshTime()

            if (now - lastRefresh < 5000) {
                return
            }

            val currenciesMap = apiService.getCurrencies()
            val currenciesSW = currenciesMap.toCurrenciesSW()

            currencyDao.clearCurrencies()
            currencyDao.insertCurrencies(currenciesSW)

            dataStorage.saveLastCurrenciesRefreshTime(now)
        }
    }

    override suspend fun getExchangeRates(baseCurrency: String): List<ExchangeRate> {
        val response = apiService.getExchangeRates(baseCurrency)
        val favoritePairs = favoriteCurrencyDao.getAllFavorites()
            .map { it.baseCurrency to it.quoteCurrency }
            .toSet()
        return response.toExchangeRates(favoritePairs)
    }

    override suspend fun getFavoritePairs(): List<ExchangeRate> {
        val favorites = favoriteCurrencyDao.getAllFavorites()

        return favorites.mapNotNull { favorite ->
            try {
                val response = apiService.getExchangeRates(
                    baseCurrency = favorite.baseCurrency,
                    symbols = favorite.quoteCurrency
                )
                val rate = response.rates[favorite.quoteCurrency] ?: return@mapNotNull null
                favorite.toExchangeRate(rate)
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun addToFavorites(baseCurrency: String, quoteCurrency: String) {
        favoriteCurrencyDao.addToFavorites(
            createFavoriteCurrencySW(baseCurrency, quoteCurrency)
        )
    }

    override suspend fun removeFromFavorites(baseCurrency: String, quoteCurrency: String) {
        favoriteCurrencyDao.removeFromFavorites(baseCurrency, quoteCurrency)
    }

    override suspend fun isFavorite(baseCurrency: String, quoteCurrency: String): Boolean {
        return favoriteCurrencyDao.isFavorite(baseCurrency, quoteCurrency)
    }

    override suspend fun getLastBaseCurrency(): String {
        return dataStorage.getLastBaseCurrency()
    }

    override suspend fun saveBaseCurrency(currencyCode: String) {
        dataStorage.saveBaseCurrency(currencyCode)
    }
}