package tech.hancharou.currencytracker.presentation.main.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import tech.hancharou.currencytracker.R
import tech.hancharou.currencytracker.presentation.main.MainViewModel
import tech.hancharou.currencytracker.presentation.theme.BottomMenuBackground
import tech.hancharou.currencytracker.presentation.theme.BottomMenuIconActive
import tech.hancharou.currencytracker.presentation.theme.BottomMenuIconInactive
import tech.hancharou.currencytracker.presentation.theme.BottomMenuTextActive
import tech.hancharou.currencytracker.presentation.theme.BottomMenuTextInactive

@Composable
fun BottomMenu(
    viewModel: MainViewModel,
    navController: NavController
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val destinationId = currentBackStack?.destination?.id

    val isCurrenciesActive = destinationId == R.id.currenciesFragment
    val isFavoritesActive = destinationId == R.id.favoritesFragment

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BottomMenuBackground)
            .padding(vertical = 8.dp)
    ) {
        BottomMenuItem(
            modifier = Modifier.weight(1f),
            isActive = isCurrenciesActive,
            iconActive = R.drawable.ic_bm_currencies_active,
            iconInactive = R.drawable.ic_bm_currencies_not_active,
            text = "Currencies",
            onClick = {
                if (!isCurrenciesActive) viewModel.goToCurrencies()
            }
        )

        BottomMenuItem(
            modifier = Modifier.weight(1f),
            isActive = isFavoritesActive,
            iconActive = R.drawable.ic_bm_favorites_active,
            iconInactive = R.drawable.ic_bm_favorites_not_active,
            text = "Favorites",
            onClick = {
                if (!isFavoritesActive) viewModel.goToFavorites()
            }
        )
    }
}

@Composable
fun BottomMenuItem(
    modifier: Modifier,
    isActive: Boolean,
    iconActive: Int,
    iconInactive: Int,
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 64.dp, height = 32.dp)
                .background(
                    color = if (isActive) BottomMenuIconActive else BottomMenuIconInactive,
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(if (isActive) iconActive else iconInactive),
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                letterSpacing = 0.sp
            ),
            color = if (isActive) BottomMenuTextActive else BottomMenuTextInactive
        )
    }
}