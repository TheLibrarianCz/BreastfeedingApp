package com.example.kojeniapp.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kojeniapp.R
import com.example.kojeniapp.database.models.DayStatisticsDataModel
import com.example.kojeniapp.viewmodels.HistoryViewModel
import java.time.LocalTime

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val historyItems by viewModel.dayStatistics.collectAsState()

    HistoryScreen(historyItems)
}

@Composable
fun HistoryScreen(historyItems: List<DayStatisticsDataModel>) {
    Column {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 176.dp)) {
            items(historyItems) {
                StatisticsCard(statistics = it)
            }
        }
    }
}

@Composable
fun StatisticsCard(statistics: DayStatisticsDataModel) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onBackground)
    ) {
        StatisticsCardTop(statistics)
        Divider()
        StatisticsCardBottom(statistics)
    }
}

@Composable
fun StatisticsCardTop(statistics: DayStatisticsDataModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 2.dp, end = 16.dp, bottom = 2.dp),
            text = stringResource(id = R.string.history_item_date_label),
            fontWeight = FontWeight.Bold,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 2.dp, end = 16.dp, bottom = 2.dp),
            text = statistics.date
        )
    }
}

@Composable
fun StatisticsCardBottom(statistics: DayStatisticsDataModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        StatisticsCardItem(
            modifier = Modifier,
            headline = stringResource(id = R.string.history_item_count_label),
            value = stringResource(
                id = R.string.history_item_times_per_day,
                statistics.feedingCount
            )
        )
        StatisticsCardItem(
            modifier = Modifier,
            headline = stringResource(id = R.string.history_item_average_label),
            value = if (statistics.averageTimeBetweenFeedings.hour > 0) {
                stringResource(
                    id = R.string.history_item_hour_and_min,
                    statistics.averageTimeBetweenFeedings.hour,
                    statistics.averageTimeBetweenFeedings.minute
                )
            } else {
                stringResource(
                    id = R.string.history_item_min_only,
                    statistics.averageTimeBetweenFeedings.minute
                )
            }
        )
        StatisticsCardItem(
            modifier = Modifier,
            headline = stringResource(id = R.string.history_item_max_label),
            value = if (statistics.maxTimeBetweenFeedings.hour > 0) {
                stringResource(
                    id = R.string.history_item_hour_and_min,
                    statistics.maxTimeBetweenFeedings.hour,
                    statistics.maxTimeBetweenFeedings.minute
                )
            } else {
                stringResource(
                    id = R.string.history_item_min_only,
                    statistics.maxTimeBetweenFeedings.minute
                )
            }
        )
    }
}

@Composable
fun StatisticsCardItem(
    modifier: Modifier,
    headline: String,
    value: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 0.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
            text = headline,
            fontWeight = FontWeight.Bold,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
            text = value
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewStatisticsCardNight() {
    StatisticsCard(
        statistics = DayStatisticsDataModel(
            date = "1.1. 1970",
            feedingCount = 2,
            averageTimeBetweenFeedings = LocalTime.now(),
            maxTimeBetweenFeedings = LocalTime.now()
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewStatisticsCardDay() {
    StatisticsCard(
        statistics = DayStatisticsDataModel(
            date = "1.1. 1970",
            feedingCount = 2,
            averageTimeBetweenFeedings = LocalTime.now(),
            maxTimeBetweenFeedings = LocalTime.now()
        )
    )
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(
        historyItems = listOf(
            DayStatisticsDataModel(
                date = "1.1. 1970",
                feedingCount = 2,
                averageTimeBetweenFeedings = LocalTime.now(),
                maxTimeBetweenFeedings = LocalTime.now()
            ),
            DayStatisticsDataModel(
                date = "3.1. 1970",
                feedingCount = 8,
                averageTimeBetweenFeedings = LocalTime.now(),
                maxTimeBetweenFeedings = LocalTime.of(0, 1)
            )
        )
    )
}
