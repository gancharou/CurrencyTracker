package tech.hancharou.currencytracker.data.sw

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_currencies")
data class FavoriteCurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val currencyCode: String
)