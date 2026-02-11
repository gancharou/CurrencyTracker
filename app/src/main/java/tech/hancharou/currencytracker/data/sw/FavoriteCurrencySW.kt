package tech.hancharou.currencytracker.data.sw

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_currencies")
data class FavoriteCurrencySW(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "baseCurrency")
    val baseCurrency: String,
    @ColumnInfo(name = "quoteCurrency")
    val quoteCurrency: String
)