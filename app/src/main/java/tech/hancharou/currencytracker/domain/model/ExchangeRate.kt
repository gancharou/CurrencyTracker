package tech.hancharou.currencytracker.domain.model

data class ExchangeRate(
    val baseCurrency: String,
    val quoteCurrency: String,
    val rate: Double,
    val isFavorite: Boolean = false
)