package tech.hancharou.currencytracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.hancharou.currencytracker.presentation.theme.BackgroundGray
import tech.hancharou.currencytracker.presentation.theme.TextHeader

@Composable
fun ScreenHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundGray)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = TextHeader,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        )
    }
}