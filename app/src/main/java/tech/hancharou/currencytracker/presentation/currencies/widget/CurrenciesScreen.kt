package tech.hancharou.currencytracker.presentation.currencies.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.collections.immutable.ImmutableList
import tech.hancharou.currencytracker.R
import tech.hancharou.currencytracker.presentation.components.CurrencyItem
import tech.hancharou.currencytracker.presentation.components.ScreenHeader
import tech.hancharou.currencytracker.presentation.currencies.CurrenciesViewModel
import tech.hancharou.currencytracker.presentation.currencies.CurrenciesViewState
import tech.hancharou.currencytracker.presentation.currencies.model.CurrenciesUI
import tech.hancharou.currencytracker.presentation.theme.BackgroundDropdownItem
import tech.hancharou.currencytracker.presentation.theme.BackgroundGray
import tech.hancharou.currencytracker.presentation.theme.BackgroundWhite
import tech.hancharou.currencytracker.presentation.theme.Border
import tech.hancharou.currencytracker.presentation.theme.PrimaryBlue
import tech.hancharou.currencytracker.presentation.theme.PrimaryBlueDark
import tech.hancharou.currencytracker.presentation.theme.ShadowColor
import tech.hancharou.currencytracker.presentation.theme.TextSecondary

@Composable
fun CurrenciesScreen(
    viewModel: CurrenciesViewModel
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is CurrenciesViewState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is CurrenciesViewState.Content -> {
            val content = state as CurrenciesViewState.Content
            CurrenciesContent(
                data = content.data,
                isRefreshing = content.isRefreshing,
                onBaseCurrencySelected = viewModel::selectBaseCurrency,
                onFavoriteToggle = viewModel::toggleFavorite,
                onRefresh = viewModel::refresh,
                onFilterClick = viewModel::onFilterClick
            )
        }

        is CurrenciesViewState.Error -> {
            val error = state as CurrenciesViewState.Error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Failed to load data",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )

                    if (error.canRetry) {
                        Button(
                            onClick = { viewModel.retry() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryBlue
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrenciesContent(
    data: CurrenciesUI,
    isRefreshing: Boolean,
    onBaseCurrencySelected: (String) -> Unit,
    onFavoriteToggle: (String, String) -> Unit,
    onRefresh: () -> Unit,
    onFilterClick: () -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var anchorOffset by remember { mutableStateOf(IntOffset.Zero) }
    var anchorWidthPx by remember { mutableStateOf(0) }

    val onToggle = remember {
        { dropdownExpanded = !dropdownExpanded }
    }

    val onAnchorMeasured = remember {
        { offset: IntOffset, widthPx: Int ->
            anchorOffset = offset
            anchorWidthPx = widthPx
        }
    }

    val onDismiss = remember {
        { dropdownExpanded = false }
    }

    val onCurrencySelectedAndDismiss = remember(onBaseCurrencySelected) {
        { currency: String ->
            onBaseCurrencySelected(currency)
            dropdownExpanded = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundWhite)
            ) {
                ScreenHeader(title = "Currencies")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundGray)
                ) {
                    CurrencyHeader(
                        baseCurrency = data.baseCurrency,
                        onFilterClick = onFilterClick,
                        expanded = dropdownExpanded,
                        onToggle = onToggle,
                        onAnchorMeasured = onAnchorMeasured
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    items(
                        items = data.exchangeRates,
                        key = { it.currencyCode }
                    ) { item ->
                        CurrencyItemWrapper(
                            item = item,
                            baseCurrency = data.baseCurrency,
                            onFavoriteToggle = onFavoriteToggle
                        )
                    }
                }
            }

            if (dropdownExpanded) {
                CurrencyDropdownOverlay(
                    selectedCurrency = data.baseCurrency,
                    availableCurrencies = data.availableCurrencies,
                    anchorOffset = anchorOffset,
                    anchorWidthPx = anchorWidthPx,
                    onDismiss = onDismiss,
                    onCurrencySelected = onCurrencySelectedAndDismiss
                )
            }
        }
    }
}

@Composable
private fun CurrencyHeader(
    baseCurrency: String,
    onFilterClick: () -> Unit,
    expanded: Boolean,
    onToggle: () -> Unit,
    onAnchorMeasured: (IntOffset, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrencyDropdownAnchor(
            selectedCurrency = baseCurrency,
            expanded = expanded,
            onToggle = onToggle,
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned { coords ->
                    val p: Offset = coords.positionInRoot()
                    onAnchorMeasured(
                        IntOffset(p.x.toInt(), (p.y + coords.size.height).toInt()),
                        coords.size.width
                    )
                }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundWhite, RoundedCornerShape(12.dp))
                .border(1.dp, Border, RoundedCornerShape(12.dp))
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sort),
                contentDescription = "Sort",
                tint = PrimaryBlueDark,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun CurrencyDropdownAnchor(
    selectedCurrency: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val corner = 8.dp
    val seam = 1.dp

    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp
    )

    val shape = if (expanded) {
        RoundedCornerShape(
            topStart = corner, topEnd = corner,
            bottomStart = 0.dp, bottomEnd = 0.dp
        )
    } else {
        RoundedCornerShape(corner)
    }

    Row(
        modifier = modifier
            .height(48.dp)
            .then(
                if (expanded) {
                    Modifier.shadow(
                        elevation = 6.dp,
                        shape = shape,
                        ambientColor = ShadowColor.copy(alpha = 0.15f),
                        spotColor = ShadowColor.copy(alpha = 0.15f)
                    )
                } else {
                    Modifier
                }
            )
            .border(1.dp, Border, shape)
            .clip(shape)
            .background(BackgroundWhite)
            .clickable { onToggle() }
            .drawWithContent {
                drawContent()
                if (expanded) {
                    val h = seam.toPx()
                    drawRect(
                        color = BackgroundWhite,
                        topLeft = Offset(0f, size.height - h),
                        size = Size(size.width, h)
                    )
                }
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(selectedCurrency, style = textStyle, color = TextSecondary)

        Icon(
            painter = painterResource(if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
            contentDescription = null,
            tint = PrimaryBlueDark,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun CurrencyDropdownOverlay(
    selectedCurrency: String,
    availableCurrencies: ImmutableList<String>,
    anchorOffset: IntOffset,
    anchorWidthPx: Int,
    onDismiss: () -> Unit,
    onCurrencySelected: (String) -> Unit
) {
    val corner = 8.dp
    val density = LocalDensity.current
    val widthDp = with(density) { anchorWidthPx.toDp() }
    val seamPx = with(density) { 1.dp.roundToPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(999f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDismiss
                )
        )

        Box(
            modifier = Modifier
                .absoluteOffset { IntOffset(anchorOffset.x, anchorOffset.y - seamPx) }
                .width(widthDp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(bottomStart = corner, bottomEnd = corner),
                    ambientColor = ShadowColor.copy(alpha = 0.15f),
                    spotColor = ShadowColor.copy(alpha = 0.15f)
                )
                .clip(RoundedCornerShape(bottomStart = corner, bottomEnd = corner))
                .background(BackgroundWhite)
                .drawWithContent {
                    drawContent()

                    val borderPx = 1.dp.toPx()
                    val cornerPx = corner.toPx()

                    drawRect(
                        color = Border,
                        topLeft = Offset(0f, 0f),
                        size = Size(borderPx, size.height - cornerPx + borderPx)
                    )

                    drawRect(
                        color = Border,
                        topLeft = Offset(size.width - borderPx, 0f),
                        size = Size(borderPx, size.height - cornerPx + borderPx)
                    )

                    drawRect(
                        color = Border,
                        topLeft = Offset(cornerPx - borderPx, size.height - borderPx),
                        size = Size(size.width - cornerPx * 2 + borderPx * 2, borderPx)
                    )

                    drawArc(
                        color = Border,
                        startAngle = 90f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(borderPx / 2, size.height - cornerPx * 2 + borderPx / 2),
                        size = Size(cornerPx * 2 - borderPx, cornerPx * 2 - borderPx),
                        style = Stroke(width = borderPx)
                    )

                    drawArc(
                        color = Border,
                        startAngle = 0f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(
                            size.width - cornerPx * 2 + borderPx / 2,
                            size.height - cornerPx * 2 + borderPx / 2
                        ),
                        size = Size(cornerPx * 2 - borderPx, cornerPx * 2 - borderPx),
                        style = Stroke(width = borderPx)
                    )
                }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp)
            ) {
                items(
                    items = availableCurrencies,
                    key = { it }
                ) { currency ->
                    DropdownItem(
                        currency = currency,
                        isSelected = currency == selectedCurrency,
                        onCurrencySelected = onCurrencySelected
                    )
                }
            }
        }
    }
}

@Composable
private fun DropdownItem(
    currency: String,
    isSelected: Boolean,
    onCurrencySelected: (String) -> Unit
) {
    val onClick = remember(currency, onCurrencySelected) {
        { onCurrencySelected(currency) }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(if (isSelected) BackgroundDropdownItem else BackgroundWhite)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = currency,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp
            ),
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun CurrencyItemWrapper(
    item: CurrenciesUI.CurrencyItem,
    baseCurrency: String,
    onFavoriteToggle: (String, String) -> Unit
) {
    val onToggle = remember(baseCurrency, item.currencyCode, onFavoriteToggle) {
        { onFavoriteToggle(baseCurrency, item.currencyCode) }
    }

    CurrencyItem(
        currencyText = item.currencyCode,
        rate = item.rate,
        isFavorite = item.isFavorite,
        onFavoriteToggle = onToggle
    )
}