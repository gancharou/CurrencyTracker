package tech.hancharou.currencytracker.presentation.sort.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.hancharou.currencytracker.domain.model.SortType
import tech.hancharou.currencytracker.presentation.sort.SortViewModel
import tech.hancharou.currencytracker.presentation.theme.BackgroundGray
import tech.hancharou.currencytracker.presentation.theme.BackgroundWhite
import tech.hancharou.currencytracker.presentation.theme.Border
import tech.hancharou.currencytracker.presentation.theme.PrimaryBlueDark
import tech.hancharou.currencytracker.presentation.theme.TextGray
import tech.hancharou.currencytracker.presentation.theme.TextHeader
import tech.hancharou.currencytracker.presentation.theme.TextSecondary

@Composable
fun SortScreen(
    viewModel: SortViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        SortHeader(onBackClick = { viewModel.cancel() })

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "SORT BY",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            ),
            color = TextGray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SortOption(
            text = "Code A-Z",
            sortType = SortType.CODE_ASC,
            selectedSort = state.selectedSort,
            onSelect = { viewModel.selectSort(it) }
        )

        SortOption(
            text = "Code Z-A",
            sortType = SortType.CODE_DESC,
            selectedSort = state.selectedSort,
            onSelect = { viewModel.selectSort(it) }
        )

        SortOption(
            text = "Quote Asc.",
            sortType = SortType.RATE_ASC,
            selectedSort = state.selectedSort,
            onSelect = { viewModel.selectSort(it) }
        )

        SortOption(
            text = "Quote Desc.",
            sortType = SortType.RATE_DESC,
            selectedSort = state.selectedSort,
            onSelect = { viewModel.selectSort(it) }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.apply() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(40.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlueDark
            )
        ) {
            Text(
                text = "Apply",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.1.sp
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortHeader(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Sort",
                style = MaterialTheme.typography.titleLarge,
                color = TextHeader
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = PrimaryBlueDark
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundGray
        )
    )
}

@Composable
private fun SortOption(
    text: String,
    sortType: SortType,
    selectedSort: SortType?,
    onSelect: (SortType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(sortType) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 20.sp,
                letterSpacing = 0.sp
            ),
            color = TextSecondary
        )

        RadioButton(
            selected = selectedSort == sortType,
            onClick = { onSelect(sortType) },
            modifier = Modifier
                .size(20.dp)
                .padding(0.dp),
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryBlueDark,
                unselectedColor = Border
            )
        )
    }
}