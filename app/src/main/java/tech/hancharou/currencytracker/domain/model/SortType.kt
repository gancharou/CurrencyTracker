package tech.hancharou.currencytracker.domain.model

enum class SortType {
    CODE_ASC,      // Код А-Я
    CODE_DESC,     // Код Я-А
    RATE_ASC,      // Курс по возрастанию
    RATE_DESC      // Курс по убыванию
}