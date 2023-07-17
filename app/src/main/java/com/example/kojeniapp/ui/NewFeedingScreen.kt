package com.example.kojeniapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kojeniapp.R
import com.example.kojeniapp.ui.theme.EspumisanBackground
import com.example.kojeniapp.ui.theme.ProbioticsBackground
import com.example.kojeniapp.ui.theme.VigantolBackground
import com.example.kojeniapp.viewmodels.NewFeedingViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun NewFeedingScreen(
    newFeedingViewModel: NewFeedingViewModel = hiltViewModel(),
    onLeaveScreen: () -> Unit
) {
    val left = remember { mutableStateOf(false) }
    val right = remember { mutableStateOf(false) }

    val vigantol = remember { mutableStateOf(false) }
    val espumisan = remember { mutableStateOf(false) }
    val probiotics = remember { mutableStateOf(false) }

    val onAddClick = {
        newFeedingViewModel.createFeeding(
            left.value,
            right.value,
            probiotics.value,
            vigantol.value,
            espumisan.value
        )
        onLeaveScreen()
    }

    NewFeedingScreen(
        left = left.value,
        right = right.value,
        vigantol = vigantol.value,
        espumisan = espumisan.value,
        probiotics = probiotics.value,
        onLeftChange = { left.value = it },
        onRightChange = { right.value = it },
        onVigantolChange = { vigantol.value = it },
        onEspumisanChange = { espumisan.value = it },
        onProbioticsChange = { probiotics.value = it },
        onAddButtonClick = onAddClick
    )
}

@Composable
fun NewFeedingScreen(
    left: Boolean,
    right: Boolean,
    vigantol: Boolean,
    espumisan: Boolean,
    probiotics: Boolean,
    onLeftChange: (Boolean) -> Unit,
    onRightChange: (Boolean) -> Unit,
    onVigantolChange: (Boolean) -> Unit,
    onEspumisanChange: (Boolean) -> Unit,
    onProbioticsChange: (Boolean) -> Unit,
    onAddButtonClick: () -> Unit
) {
    val addEnabled: Boolean = left || right
    Box {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            NewFeedingContent(
                left = left,
                right = right,
                vigantol = vigantol,
                espumisan = espumisan,
                probiotics = probiotics,
                onLeftChange = onLeftChange,
                onRightChange = onRightChange,
                onVigantolChange = onVigantolChange,
                onEspumisanChange = onEspumisanChange,
                onProbioticsChange = onProbioticsChange
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { if (addEnabled) onAddButtonClick() },
            backgroundColor = if (addEnabled) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.secondary
            },
            interactionSource = if (addEnabled) {
                remember { MutableInteractionSource() }
            } else {
                BlockInteractionSource()
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

private class BlockInteractionSource : MutableInteractionSource {
    override val interactions = MutableSharedFlow<Interaction>()

    override suspend fun emit(interaction: Interaction) = Unit

    override fun tryEmit(interaction: Interaction): Boolean = false
}

@Composable
fun NewFeedingContent(
    left: Boolean,
    right: Boolean,
    vigantol: Boolean,
    espumisan: Boolean,
    probiotics: Boolean,
    onLeftChange: (Boolean) -> Unit,
    onRightChange: (Boolean) -> Unit,
    onVigantolChange: (Boolean) -> Unit,
    onEspumisanChange: (Boolean) -> Unit,
    onProbioticsChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.feeding_next_feeding_header),
            fontWeight = FontWeight.Bold,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.clickable { onLeftChange(!left) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.feeding_left_breast_label))
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.ic_left_breast),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Checkbox(
                    checked = left,
                    onCheckedChange = onLeftChange,
                    colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.onBackground)
                )
            }
            Row(
                modifier = Modifier.clickable { onRightChange(!right) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.feeding_right_breast_label))
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.ic_right_breast),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Checkbox(
                    checked = right,
                    onCheckedChange = onRightChange,
                    colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.onBackground)
                )
            }
        }
        Column {
            Text(
                text = stringResource(id = R.string.feeding_additions_label),
                fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize
            )
            Divider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onVigantolChange(!vigantol) }
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = stringResource(id = R.string.feeding_vigantol_label)
                )
                Checkbox(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = vigantol,
                    onCheckedChange = onVigantolChange,
                    colors = CheckboxDefaults.colors(

                        uncheckedColor = MaterialTheme.colorScheme.onBackground,
                        checkedColor = VigantolBackground
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEspumisanChange(!espumisan) }
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = stringResource(id = R.string.feeding_espumisan_label)
                )
                Checkbox(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = espumisan,
                    onCheckedChange = onEspumisanChange,
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = MaterialTheme.colorScheme.onBackground,
                        checkedColor = EspumisanBackground
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProbioticsChange(!probiotics) }
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = stringResource(id = R.string.feeding_probiotics_label)
                )
                Checkbox(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = probiotics,
                    onCheckedChange = onProbioticsChange,
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = MaterialTheme.colorScheme.onBackground,
                        checkedColor = ProbioticsBackground
                    )
                )
            }
        }
    }
}

@Composable
fun NewFeedingDialog(
    onConfirm: (Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit,
    onCancel: () -> Unit
) {
    val left = remember { mutableStateOf(false) }
    val right = remember { mutableStateOf(false) }

    val vigantol = remember { mutableStateOf(false) }
    val espumisan = remember { mutableStateOf(false) }
    val probiotics = remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
            ) {
                NewFeedingContent(
                    left = left.value,
                    right = right.value,
                    vigantol = vigantol.value,
                    espumisan = espumisan.value,
                    probiotics = probiotics.value,
                    onLeftChange = { left.value = it },
                    onRightChange = { right.value = it },
                    onVigantolChange = { vigantol.value = it },
                    onEspumisanChange = { espumisan.value = it },
                    onProbioticsChange = { probiotics.value = it }
                )
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledBackgroundColor = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 16.dp, end = 16.dp),
                    enabled = left.value || right.value,
                    onClick = {
                        onConfirm(
                            left.value,
                            right.value,
                            probiotics.value,
                            vigantol.value,
                            espumisan.value
                        )
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.feeding_safe_dialog_button),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewFeedingContent() {
    NewFeedingContent(
        left = true,
        right = false,
        vigantol = true,
        espumisan = true,
        probiotics = true,
        onLeftChange = { },
        onRightChange = { },
        onVigantolChange = { },
        onEspumisanChange = { },
        onProbioticsChange = { }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNewFeedingDialog() {
    NewFeedingDialog(
        onConfirm = { _, _, _, _, _ -> },
        onCancel = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNewFeedingScreen() {
    NewFeedingScreen(
        left = true,
        right = false,
        vigantol = true,
        espumisan = true,
        probiotics = true,
        onLeftChange = { },
        onRightChange = { },
        onVigantolChange = { },
        onEspumisanChange = { },
        onProbioticsChange = { },
        onAddButtonClick = { }
    )
}
