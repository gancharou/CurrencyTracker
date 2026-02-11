package tech.hancharou.currencytracker.data.nw

data class ExchangeRatesNW(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)