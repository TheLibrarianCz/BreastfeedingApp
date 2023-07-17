package com.example.kojeniapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kojeniapp.R
import com.example.kojeniapp.database.models.AdditionalConstants
import com.example.kojeniapp.database.models.BreastType
import com.example.kojeniapp.database.models.Feeding
import com.example.kojeniapp.database.models.LastFeedingDataModel
import com.example.kojeniapp.ui.theme.EspumisanBackground
import com.example.kojeniapp.ui.theme.ProbioticsBackground
import com.example.kojeniapp.ui.theme.VigantolBackground
import com.example.kojeniapp.utils.DateUtils
import com.example.kojeniapp.utils.FlagUtils
import com.example.kojeniapp.viewmodels.FeedingUiState
import com.example.kojeniapp.viewmodels.FeedingViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TodayScreen(
    viewModel: FeedingViewModel = hiltViewModel(),
    onToNewFeedingScreen: () -> Unit
) {
    val uiState by viewModel.feedingUiState.collectAsState()

    val useDialog by viewModel.useDialog.collectAsState()

    when (uiState) {
        is FeedingUiState.Loaded -> {
            TodayScreen(
                uiState = uiState as FeedingUiState.Loaded,
                useDialog = useDialog,
                createFeeding = viewModel::createFeeding,
                onToNewFeedingScreen = onToNewFeedingScreen
            )
        }

        FeedingUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun TodayScreen(
    uiState: FeedingUiState.Loaded,
    useDialog: Boolean,
    createFeeding: (Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit,
    onToNewFeedingScreen: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }

    TodayScreen(
        feedings = uiState.feedings,
        lastFeeding = uiState.lastFeedingDataModel,
        showDialog = showDialog.value,
        onFloatingButtonClick = {
            if (useDialog) {
                showDialog.value = true
            } else {
                onToNewFeedingScreen()
            }
        },
        onDialogConfirm = { left, right, vig, espu, pro ->
            createFeeding(left, right, vig, espu, pro)
            showDialog.value = false
        },
        onDialogCancel = {
            showDialog.value = false
        }
    )
}

@Composable
fun TodayScreen(
    feedings: List<Feeding>,
    lastFeeding: LastFeedingDataModel,
    showDialog: Boolean,
    onFloatingButtonClick: () -> Unit,
    onDialogConfirm: (Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit,
    onDialogCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            if (!lastFeeding.isEmpty) {
                LastFeedingCard(lastFeeding)
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(feedings) { feeding ->
                    FeedingRow(feeding)
                }
            }
        }
        if (showDialog) {
            NewFeedingDialog(
                onConfirm = onDialogConfirm,
                onCancel = onDialogCancel
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onFloatingButtonClick,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}

@Composable
fun LastFeedingCard(feeding: LastFeedingDataModel) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 2.dp),
                    text = stringResource(id = R.string.next_feeding_card_last_feeding),
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 2.dp, end = 16.dp, bottom = 2.dp),
                    text = feeding.date
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 2.dp, end = 16.dp, bottom = 2.dp),
                    text = feeding.timestamp
                )
            }

            Divider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BreastIcon(breast = feeding.breast)
                Divider(
                    modifier = Modifier
                        .height(56.dp)
                        .width(1.dp)
                        .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                if (feeding.hasEspumisan()) {
                    EspumisanChip()
                }
                if (feeding.hasVigantol()) {
                    VigantolChip()
                }
                if (feeding.hasProbiotics()) {
                    ProbioticsChip()
                }
            }

            Divider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 2.dp),
                    text = stringResource(id = R.string.next_feeding_card_next_feeding),
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
                if (feeding.isNextFeedingTomorrow) {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 2.dp, end = 16.dp, bottom = 2.dp),
                        text = stringResource(id = R.string.history_item_tomorrow)
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 2.dp, end = 16.dp, bottom = 2.dp),
                    text = feeding.nextFeeding
                )
            }
        }
    }
}

@Composable
fun EspumisanChip() {
    AdditionsChip(
        stringResource(id = R.string.feeding_espumisan_label),
        EspumisanBackground,
        Color.White
    )
}

@Composable
fun VigantolChip() {
    AdditionsChip(
        stringResource(id = R.string.feeding_vigantol_label),
        VigantolBackground,
        Color.White
    )
}

@Composable
fun ProbioticsChip() {
    AdditionsChip(
        stringResource(id = R.string.feeding_probiotics_label),
        ProbioticsBackground,
        Color.Black
    )
}

@Composable
fun AdditionsChip(
    text: String,
    background: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = CardDefaults.outlinedShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Row(
            modifier = Modifier.background(color = background)
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = text,
                color = textColor
            )
        }
    }
}

@Composable
fun FeedingRow(feeding: Feeding) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            text = feeding.timestamp.format(DateUtils.getTimeFormatter()),
            fontWeight = FontWeight.Bold,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            textAlign = TextAlign.Center
        )
        Divider(
            modifier = Modifier
                .height(56.dp)
                .width(1.dp)
                .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        BreastIcon(breast = feeding.breast)
        Divider(
            modifier = Modifier
                .height(56.dp)
                .width(1.dp)
                .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        if (FlagUtils.HasFlag(feeding.additions, AdditionalConstants.ESPUMISAN)) {
            EspumisanChip()
        }
        if (FlagUtils.HasFlag(feeding.additions, AdditionalConstants.VIGANTOL)) {
            VigantolChip()
        }
        if (FlagUtils.HasFlag(feeding.additions, AdditionalConstants.PROBIOTICS)) {
            ProbioticsChip()
        }
    }
}

@Composable
fun BreastIcon(
    breast: Int,
    showLabel: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = when (breast) {
                BreastType.LEFT -> painterResource(id = R.drawable.ic_left_breast)
                BreastType.RIGHT -> painterResource(id = R.drawable.ic_right_breast)
                BreastType.LEFT + BreastType.RIGHT -> painterResource(id = R.drawable.ic_both_breast)
                else -> painterResource(id = R.drawable.ic_question_mark)
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        if (showLabel) {
            Text(
                text = when (breast) {
                    BreastType.LEFT -> stringResource(id = R.string.feeding_left_breast_label)
                    BreastType.RIGHT -> stringResource(id = R.string.feeding_right_breast_label)
                    BreastType.LEFT + BreastType.RIGHT -> stringResource(id = R.string.feeding_both_breasts_label)
                    else -> "?"
                },
                fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBreastIcon() {
    Column {
        BreastIcon(breast = BreastType.LEFT)
        BreastIcon(breast = BreastType.RIGHT)
        BreastIcon(breast = BreastType.LEFT + BreastType.RIGHT)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFeedingRows() {
    Column {
        FeedingRow(
            feeding = Feeding(
                id = 1,
                date = LocalDate.now(),
                timestamp = LocalTime.now(),
                breast = BreastType.LEFT,
                additions = AdditionalConstants.PROBIOTICS
            )
        )
        FeedingRow(
            feeding = Feeding(
                id = 2,
                date = LocalDate.now(),
                timestamp = LocalTime.now(),
                breast = BreastType.RIGHT,
                additions = AdditionalConstants.ESPUMISAN
            )
        )
        FeedingRow(
            feeding = Feeding(
                id = 3,
                date = LocalDate.now(),
                timestamp = LocalTime.now(),
                breast = BreastType.LEFT + BreastType.RIGHT,
                additions = AdditionalConstants.VIGANTOL
            )
        )
        FeedingRow(
            feeding = Feeding(
                id = 4,
                date = LocalDate.now(),
                timestamp = LocalTime.now(),
                breast = BreastType.LEFT + BreastType.RIGHT,
                additions = listOf(
                    AdditionalConstants.ESPUMISAN,
                    AdditionalConstants.PROBIOTICS,
                    AdditionalConstants.VIGANTOL
                ).sum()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLastFeedingCard() {
    LastFeedingCard(
        feeding = LastFeedingDataModel(
            feeding = Feeding(
                id = 4,
                date = LocalDate.now(),
                timestamp = LocalTime.now(),
                breast = BreastType.LEFT + BreastType.RIGHT,
                additions = listOf(
                    AdditionalConstants.ESPUMISAN,
                    AdditionalConstants.PROBIOTICS,
                    AdditionalConstants.VIGANTOL
                ).sum()
            ),
            nextFeedingHourInterval = 2
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTodayScreen() {
    val feedings = listOf(
        Feeding(
            id = 1,
            date = LocalDate.now(),
            timestamp = LocalTime.now(),
            breast = BreastType.LEFT,
            additions = AdditionalConstants.PROBIOTICS
        ),
        Feeding(
            id = 2,
            date = LocalDate.now(),
            timestamp = LocalTime.now(),
            breast = BreastType.RIGHT,
            additions = AdditionalConstants.ESPUMISAN
        ),
        Feeding(
            id = 3,
            date = LocalDate.now(),
            timestamp = LocalTime.now(),
            breast = BreastType.LEFT + BreastType.RIGHT,
            additions = AdditionalConstants.VIGANTOL
        ),
        Feeding(
            id = 4,
            date = LocalDate.now(),
            timestamp = LocalTime.now(),
            breast = BreastType.LEFT + BreastType.RIGHT,
            additions = listOf(
                AdditionalConstants.ESPUMISAN,
                AdditionalConstants.PROBIOTICS,
                AdditionalConstants.VIGANTOL
            ).sum()
        )
    )

    TodayScreen(
        feedings = feedings,
        lastFeeding = LastFeedingDataModel(
            feeding = feedings.first(),
            nextFeedingHourInterval = 2
        ),
        showDialog = true,
        onFloatingButtonClick = { },
        onDialogConfirm = { _, _, _, _, _ -> },
        onDialogCancel = { }
    )
}
