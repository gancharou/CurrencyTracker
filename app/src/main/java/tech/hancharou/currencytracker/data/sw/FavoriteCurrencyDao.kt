package tech.hancharou.currencytracker.data.sw

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteCurrencyDao {
    
    @Query("SELECT * FROM favorite_currencies")
    suspend fun getAllFavorites(): List<FavoriteCurrencyEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(currency: FavoriteCurrencyEntity)
    
    @Query("DELETE FROM favorite_currencies WHERE currencyCode = :currencyCode")
    suspend fun removeFromFavorites(currencyCode: String)
}