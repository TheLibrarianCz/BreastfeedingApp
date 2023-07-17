package com.example.kojeniapp.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kojeniapp.R
import com.example.kojeniapp.ui.theme.KojeniTheme

@Composable
fun KojeniApp() {
    KojeniTheme {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            bottomBar = { KojeniBottomNavBar(navController) },
            topBar = { KojeniTopAppBar(navController) },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            KojeniNavGraph(
                paddingValues,
                navController,
                snackbarHostState,
                KojeniAppDestinations.TODAY_ROUTE
            )
        }
    }
}

@Composable
fun KojeniTopAppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isTopLevelDestination = currentDestination?.route in setOf(
        KojeniAppDestinations.TODAY_ROUTE,
        KojeniAppDestinations.HISTORY_ROUTE
    )
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary,
        actions = {
            if (isTopLevelDestination) {
                IconButton(onClick = { navController.navigate(KojeniAppDestinations.SETTINGS_ROUTE) }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        title = {
            val titleText = when (currentDestination?.route) {
                KojeniAppDestinations.TODAY_ROUTE -> stringResource(id = R.string.nav_item_breastfeeding)
                KojeniAppDestinations.HISTORY_ROUTE -> stringResource(id = R.string.nav_item_history)
                KojeniAppDestinations.NEW_FEEDING_ROUTE -> stringResource(id = R.string.feeding_next_feeding_header)
                KojeniAppDestinations.SETTINGS_ROUTE -> stringResource(id = R.string.settings_title)
                else -> null
            }
            if (titleText != null) {
                Text(
                    text = titleText,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
        },
        navigationIcon = {
            if (!isTopLevelDestination) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
fun KojeniNavGraph(
    paddingValues: PaddingValues,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    startingDestination: String
) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = startingDestination
    ) {
        composable(route = KojeniAppDestinations.TODAY_ROUTE) {
            TodayScreen(
                onToNewFeedingScreen = {
                    navController.navigate(KojeniAppDestinations.NEW_FEEDING_ROUTE) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = KojeniAppDestinations.HISTORY_ROUTE) {
            HistoryScreen()
        }

        composable(route = KojeniAppDestinations.NEW_FEEDING_ROUTE) {
            NewFeedingScreen(onLeaveScreen = {
                navController.popBackStack()
            })
        }

        composable(route = KojeniAppDestinations.SETTINGS_ROUTE) {
            SettingsScreen(snackbarHostState = snackbarHostState)
        }
    }
}

@Composable
fun KojeniBottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val onNavItemClick: (String) -> Unit = { route ->
        if (currentDestination?.route != route) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    }

    if (currentDestination?.route in setOf(
            KojeniAppDestinations.TODAY_ROUTE,
            KojeniAppDestinations.HISTORY_ROUTE
        )
    ) {
        BottomAppBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
            BottomNavigationBarItem(
                label = stringResource(id = R.string.nav_item_breastfeeding),
                icon = Icons.Default.Home,
                onClick = { onNavItemClick(KojeniAppDestinations.TODAY_ROUTE) },
                selected = currentDestination?.hierarchy?.any {
                    it.route == KojeniAppDestinations.TODAY_ROUTE
                } == true
            )

            BottomNavigationBarItem(
                label = stringResource(id = R.string.nav_item_history),
                icon = Icons.Default.List,
                onClick = { onNavItemClick(KojeniAppDestinations.HISTORY_ROUTE) },
                selected = currentDestination?.hierarchy?.any {
                    it.route == KojeniAppDestinations.HISTORY_ROUTE
                } == true
            )
        }
    }
}

@Composable
fun RowScope.BottomNavigationBarItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    selected: Boolean
) {
    BottomNavigationItem(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) {
                    MaterialTheme.colorScheme.surfaceTint
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) {
                    MaterialTheme.colorScheme.surfaceTint
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    )
}

object KojeniAppDestinations {
    const val TODAY_ROUTE = "today"
    const val HISTORY_ROUTE = "history"

    const val NEW_FEEDING_ROUTE = "new_feeding"

    const val SETTINGS_ROUTE = "settings"
}
