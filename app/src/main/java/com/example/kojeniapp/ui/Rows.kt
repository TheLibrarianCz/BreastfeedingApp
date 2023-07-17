package com.example.kojeniapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kojeniapp.R
import kotlin.math.roundToInt

@Composable
fun AppRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    painter: Painter? = null,
    rowClickable: Boolean = false,
    onRowClick: () -> Unit = {},
    endContent: @Composable (BoxScope.() -> Unit)? = null
) {
    BaseAppRow(
        modifier = modifier,
        title = title,
        description = description,
        icon = null,
        painter = painter,
        rowClickable = rowClickable,
        onRowClick = onRowClick,
        endContent = endContent
    )
}

@Composable
fun AppRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    rowClickable: Boolean = false,
    onRowClick: () -> Unit = {},
    endContent: @Composable (BoxScope.() -> Unit)? = null
) {
    BaseAppRow(
        modifier = modifier,
        title = title,
        description = description,
        icon = icon,
        painter = null,
        rowClickable = rowClickable,
        onRowClick = onRowClick,
        endContent = endContent
    )
}

@Composable
private fun BaseAppRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    painter: Painter? = null,
    icon: ImageVector? = null,
    rowClickable: Boolean = false,
    onRowClick: () -> Unit = {},
    bottomContent: @Composable (ColumnScope.() -> Unit)? = null,
    endContent: @Composable (BoxScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .defaultMinSize(
                minWidth = 0.dp,
                minHeight = 56.dp
            )
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clickable(onClick = onRowClick, enabled = rowClickable)
    ) {
        AppRowIcon(
            icon = icon,
            painter = painter
        )

        AppRowTitleColumn(
            title = title,
            description = description,
            bottomContent = bottomContent,
            hasIcon = icon != null || painter != null
        )

        if (endContent != null) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 16.dp)
            ) {
                endContent()
            }
        }
    }
}

@Composable
private fun RowScope.AppRowIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    painter: Painter? = null
) {
    if (painter != null || icon != null) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp)
        ) {
            val iconModifier = modifier
                .align(Alignment.Center)
                .size(24.dp)
            when {
                icon != null -> {
                    Icon(
                        modifier = iconModifier,
                        imageVector = icon,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = null
                    )
                }

                painter != null -> {
                    Icon(
                        modifier = iconModifier,
                        painter = painter,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = null
                    )
                }

                else -> throw IllegalArgumentException()
            }
        }
    }
}

@Composable
private fun RowScope.AppRowTitleColumn(
    title: String,
    description: String? = null,
    hasIcon: Boolean,
    bottomContent: @Composable (ColumnScope.() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
            if (description != null) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = description,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
            if (bottomContent != null) {
                bottomContent()
            }
        }
    }
}

@Composable
fun SliderAppRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
) {
    BaseAppRow(
        modifier = modifier,
        title = title,
        description = description,
        icon = icon,
        painter = null,
        rowClickable = false,
        onRowClick = { },
        bottomContent = {
            Slider(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                value = value.toFloat(),
                onValueChange = {
                    onValueChange(it.roundToInt())
                },
                steps = steps,
                valueRange = valueRange
            )
        },
        endContent = null
    )
}

@Composable
fun SliderAppRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    painter: Painter? = null,
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
) {
    BaseAppRow(
        modifier = modifier,
        title = title,
        description = description,
        icon = null,
        painter = painter,
        rowClickable = false,
        onRowClick = { },
        bottomContent = {
            Slider(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                value = value.toFloat(),
                onValueChange = {
                    onValueChange(it.roundToInt())
                },
                steps = steps,
                valueRange = valueRange
            )
        },
        endContent = null
    )
}

@Composable
fun SwitchAppRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val onRowClick = { onCheckedChange(!checked) }
    AppRow(
        modifier = modifier,
        title = title,
        description = description,
        icon = icon,
        rowClickable = true,
        onRowClick = onRowClick
    ) {
        Switch(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SwitchAppRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    painter: Painter? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val onRowClick = { onCheckedChange(!checked) }
    AppRow(
        modifier = modifier,
        title = title,
        description = description,
        painter = painter,
        rowClickable = true,
        onRowClick = onRowClick
    ) {
        Switch(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun ProgressAppRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    rowClickable: Boolean = false,
    onRowClick: () -> Unit = {},
    showProgress: Boolean
) {
    AppRow(
        modifier = modifier,
        title = title,
        description = description,
        icon = icon,
        rowClickable = rowClickable,
        onRowClick = onRowClick,
        endContent = if (showProgress) {
            {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            null
        }
    )
}

@Composable
fun ProgressAppRow(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    painter: Painter? = null,
    rowClickable: Boolean = false,
    onRowClick: () -> Unit = {},
    showProgress: Boolean
) {
    AppRow(
        modifier = modifier,
        title = title,
        description = description,
        painter = painter,
        rowClickable = rowClickable,
        onRowClick = onRowClick,
        endContent = if (showProgress) {
            {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            null
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun AppRowPreview() {
    Column {
        AppRow(
            title = "Dummy title",
            description = "Dummy description",
            icon = Icons.Default.Home
        )
        AppRow(
            title = "Dummy title",
            icon = Icons.Default.Home
        )
        AppRow(
            title = "Dummy title",
            description = "Dummy description",
            icon = null
        )
        AppRow(
            title = "Dummy title",
            painter = null
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SwitchAppRowPreview() {
    Column {
        SwitchAppRow(
            title = "SwitchAppRow",
            description = "Checked",
            icon = null,
            checked = true,
            onCheckedChange = {}
        )
        SwitchAppRow(
            title = "SwitchAppRow",
            icon = Icons.Default.Home,
            description = "Unchecked",
            checked = false,
            onCheckedChange = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProgressAppRowPreview() {
    Column {
        ProgressAppRow(
            title = "ProgressRow",
            icon = Icons.Default.Home,
            description = "Show",
            showProgress = true
        )
        ProgressAppRow(
            title = "ProgressRow",
            icon = Icons.Default.Home,
            description = "Hide",
            showProgress = false
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SliderAppRowPreview() {
    Column {
        SliderAppRow(
            value = 2,
            onValueChange = { },
            title = "Slider 1",
            description = "Description",
            painter = painterResource(id = R.drawable.ic_chart),
            valueRange = 0f..4f,
            steps = 5
        )
        SliderAppRow(
            value = 2,
            onValueChange = { },
            title = "Slider 1",
            painter = null,
            valueRange = 0f..4f,
            steps = 5
        )
    }
}
