package tech.hancharou.currencytracker.data.sw

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteCurrencySW::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCurrencyDao(): FavoriteCurrencyDao
}