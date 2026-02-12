package tech.hancharou.currencytracker.data.sw

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    
    @Query("SELECT * FROM currencies")
    suspend fun getAllCurrencies(): List<CurrencySW>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencySW>)
    
    @Query("DELETE FROM currencies")
    suspend fun clearCurrencies()
}