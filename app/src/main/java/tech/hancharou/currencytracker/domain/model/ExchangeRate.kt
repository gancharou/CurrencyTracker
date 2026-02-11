package tech.hancharou.currencytracker.domain.model

data class ExchangeRate(
    val currencyCode: String,
    val rate: Double,
    val isFavorite: Boolean = false
)