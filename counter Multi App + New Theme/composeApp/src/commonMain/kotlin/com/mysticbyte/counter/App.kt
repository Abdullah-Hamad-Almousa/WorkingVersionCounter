package com.mysticbyte.counter

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mysticbyte.counter.di.appModule
import com.mysticbyte.counter.ui.screens.ComingSoonScreen
import com.mysticbyte.counter.ui.screens.CounterDetailScreen
import com.mysticbyte.counter.ui.screens.HistoryScreen
import com.mysticbyte.counter.ui.screens.HomeScreen
import com.mysticbyte.counter.ui.screens.LauncherScreen
import com.mysticbyte.counter.ui.screens.SettingsScreen
import com.mysticbyte.counter.ui.theme.CounterTheme
import com.mysticbyte.counter.ui.viewmodel.CountersViewModel
import com.mysticbyte.counter.ui.viewmodel.SettingsViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object LauncherRoute

@Serializable
object HomeRoute

@Serializable
data class DetailRoute(val counterId: String)

@Serializable
data class HistoryRoute(val counterId: String, val counterName: String)

@Serializable
object SettingsRoute

@Serializable
object ComingSoonRoute

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val settingsViewModel: SettingsViewModel = koinViewModel()
        val theme by settingsViewModel.theme.collectAsState()

        CounterTheme(appTheme = theme) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = LauncherRoute,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                }
            ) {
                composable<LauncherRoute> {
                    LauncherScreen(
                        onLaunchApp = { appId ->
                            when (appId) {
                                "counter" -> navController.navigate(HomeRoute)
                                else -> navController.navigate(ComingSoonRoute)
                            }
                        }
                    )
                }
                composable<HomeRoute> {
                    HomeScreen(
                        onNavigateToDetail = { counterId ->
                            navController.navigate(DetailRoute(counterId))
                        },
                        onNavigateToSettings = {
                            navController.navigate(SettingsRoute)
                        },
                        onNavigateToMenu = {
                            navController.popBackStack(LauncherRoute, inclusive = false)
                        }
                    )
                }
                composable<DetailRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<DetailRoute>()
                    val countersViewModel: CountersViewModel = koinViewModel()
                    val counters by countersViewModel.counters.collectAsState()
                    val counter = counters.find { it.id == route.counterId }
                    CounterDetailScreen(
                        counterId = route.counterId,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToHistory = {
                            counter?.let {
                                navController.navigate(HistoryRoute(it.id, it.name))
                            }
                        }
                    )
                }
                composable<HistoryRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<HistoryRoute>()
                    HistoryScreen(
                        counterId = route.counterId,
                        counterName = route.counterName,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<SettingsRoute> {
                    SettingsScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<ComingSoonRoute> {
                    ComingSoonScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
