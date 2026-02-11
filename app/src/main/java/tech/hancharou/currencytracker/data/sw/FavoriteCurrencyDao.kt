package tech.hancharou.currencytracker.data.sw

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteCurrencyDao {

    @Query("SELECT * FROM favorite_currencies")
    suspend fun getAllFavorites(): List<FavoriteCurrencySW>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(currency: FavoriteCurrencySW)

    @Query("DELETE FROM favorite_currencies WHERE baseCurrency = :baseCurrency AND quoteCurrency = :quoteCurrency")
    suspend fun removeFromFavorites(baseCurrency: String, quoteCurrency: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_currencies WHERE baseCurrency = :baseCurrency AND quoteCurrency = :quoteCurrency)")
    suspend fun isFavorite(baseCurrency: String, quoteCurrency: String): Boolean
}