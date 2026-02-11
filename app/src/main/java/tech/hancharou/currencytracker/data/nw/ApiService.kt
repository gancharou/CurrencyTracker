package tech.hancharou.currencytracker.data.nw

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    
    @GET("v1/currencies")
    suspend fun getCurrencies(): Map<String, String>
    
    @GET("v1/latest")
    suspend fun getExchangeRates(
        @Query("base") baseCurrency: String
    ): ExchangeRatesNW
}