package tech.hancharou.currencytracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.hancharou.currencytracker.R
import tech.hancharou.currencytracker.presentation.theme.Border
import tech.hancharou.currencytracker.presentation.theme.Favorite
import tech.hancharou.currencytracker.presentation.theme.PrimaryBlueLight
import tech.hancharou.currencytracker.presentation.theme.TextSecondary

@Composable
fun CurrencyItem(
    currencyText: String,
    rate: String,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onToggle = remember(onFavoriteToggle) {
        { onFavoriteToggle() }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = PrimaryBlueLight,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 14.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currencyText,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp
            ),
            color = TextSecondary
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rate,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp
                ),
                color = TextSecondary
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onToggle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (isFavorite) {
                        painterResource(R.drawable.ic_favorites_on)
                    } else {
                        painterResource(R.drawable.ic_favorites_off)
                    },
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Favorite else Border,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}