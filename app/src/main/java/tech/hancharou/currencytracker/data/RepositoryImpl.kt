package tech.hancharou.currencytracker.data

import javax.inject.Inject
import tech.hancharou.currencytracker.data.nw.ApiService
import tech.hancharou.currencytracker.data.sw.AppDatabase
import tech.hancharou.currencytracker.data.sw.FavoriteCurrencyEntity
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

    override suspend fun getCurrencies(): List<Currency> {
        return apiService.getCurrencies().toCurrencies()
    }

    override suspend fun getExchangeRates(baseCurrency: String): List<ExchangeRate> {
        val response = apiService.getExchangeRates(baseCurrency)
        val favorites = getFavoriteCurrencies().toSet()
        return response.toExchangeRates(favorites)
    }

    override suspend fun getFavoriteCurrencies(): List<String> {
        return favoriteCurrencyDao.getAllFavorites().map { it.currencyCode }
    }

    override suspend fun addToFavorites(currencyCode: String) {
        favoriteCurrencyDao.addToFavorites(FavoriteCurrencyEntity(currencyCode = currencyCode))
    }

    override suspend fun removeFromFavorites(currencyCode: String) {
        favoriteCurrencyDao.removeFromFavorites(currencyCode)
    }

    override suspend fun getLastBaseCurrency(): String {
        return dataStorage.getLastBaseCurrency()
    }

    override suspend fun saveBaseCurrency(currencyCode: String) {
        dataStorage.saveBaseCurrency(currencyCode)
    }
}