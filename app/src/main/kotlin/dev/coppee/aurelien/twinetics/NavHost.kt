/*
 * Copyright (C) 2025 Aurélien Coppée
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.coppee.aurelien.twinetics

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.coppee.aurelien.twinetics.core.auth.LocalAuthHelper
import dev.coppee.aurelien.twinetics.core.designsystem.LocalSharedTransitionScope
import dev.coppee.aurelien.twinetics.core.feedback.feedbackDialog
import dev.coppee.aurelien.twinetics.core.user.Rn3User.LoggedOutUser
import dev.coppee.aurelien.twinetics.feature.connect.connectScreen
import dev.coppee.aurelien.twinetics.feature.connect.navigateToConnect
import dev.coppee.aurelien.twinetics.feature.events.eventsScreen
import dev.coppee.aurelien.twinetics.feature.events.navigateToEvents
import dev.coppee.aurelien.twinetics.feature.feed.friends.friendsFeedScreen
import dev.coppee.aurelien.twinetics.feature.feed.friends.navigateToFriendsFeed
import dev.coppee.aurelien.twinetics.feature.feed.publics.navigateToPublicFeed
import dev.coppee.aurelien.twinetics.feature.feed.publics.publicFeedScreen
import dev.coppee.aurelien.twinetics.feature.information.INFORMATION_ROUTE
import dev.coppee.aurelien.twinetics.feature.information.informationScreen
import dev.coppee.aurelien.twinetics.feature.information.navigateToInformation
import dev.coppee.aurelien.twinetics.feature.login.LOGIN_ROUTE
import dev.coppee.aurelien.twinetics.feature.login.loginScreen
import dev.coppee.aurelien.twinetics.feature.login.navigateToLogin
import dev.coppee.aurelien.twinetics.feature.publication.navigateToPublication
import dev.coppee.aurelien.twinetics.feature.publication.publicationScreen
import dev.coppee.aurelien.twinetics.feature.settings.navigateToSettings
import dev.coppee.aurelien.twinetics.feature.settings.settingsNavigation
import dev.coppee.aurelien.twinetics.ui.AppState
import kotlinx.coroutines.delay
import coppee.aurelien.twinetics.R.color

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DesignSystem")
@Composable
fun NavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    routes: List<String>,
) {
    val auth = LocalAuthHelper.current
    var pageCount by remember { mutableIntStateOf(0) }

    val navController = appState.navController

    LaunchedEffect(Unit) {
        delay(timeMillis = 100)
        navController.navigate(route = if (auth.getUser() is LoggedOutUser) LOGIN_ROUTE else routes[pageCount]) {
            if (pageCount < routes.size - 1) pageCount + 1
            popUpTo(route = "SPLASHSCREEN_SET_ROUTE") { inclusive = true }
        }
    }

    Scaffold {
        SharedTransitionLayout {
            CompositionLocalProvider(value = LocalSharedTransitionScope provides this) {
                NavHost(
                    navController = navController,
                    startDestination = "SPLASHSCREEN_SET_ROUTE",
                    modifier = modifier,
                ) {
                    connectScreen(
                        navController = navController,
                        navigateToSettings = navController::navigateToSettings,
                        navigateToFriends = { navController.navigateToFriendsFeed {} },
                        navigateToPublication = navController::navigateToPublication,
                        navigateToPublic = { navController.navigateToPublicFeed {} },
                        navigateToEvents = navController::navigateToEvents,
                        navigateToLogin = navController::navigateToLogin,
                    )
                    friendsFeedScreen(
                        navController = navController,
                        navigateToSettings = navController::navigateToSettings,
                        navigateToConnect = navController::navigateToConnect,
                        navigateToPublication = navController::navigateToPublication,
                        navigateToPublic = { navController.navigateToPublicFeed {} },
                        navigateToEvents = navController::navigateToEvents,
                    )
                    publicFeedScreen(
                        navController = navController,
                        navigateToSettings = navController::navigateToSettings,
                        navigateToConnect = navController::navigateToConnect,
                        navigateToFriends = { navController.navigateToFriendsFeed {} },
                        navigateToPublication = navController::navigateToPublication,
                        navigateToEvents = navController::navigateToEvents,
                    )
                    eventsScreen(
                        navController = navController,
                        navigateToSettings = navController::navigateToSettings,
                        navigateToConnect = navController::navigateToConnect,
                        navigateToFriends = { navController.navigateToFriendsFeed {} },
                        navigateToPublication = navController::navigateToPublication,
                        navigateToPublic = { navController.navigateToPublicFeed {} },
                    )

                    publicationScreen(
                        navController = navController,
                        navigateToSettings = navController::navigateToSettings,
                        navigateToPublic = { navController.navigateToPublicFeed {} },
                        navigateToFriends = { navController.navigateToFriendsFeed {} },
                        navigateToEvents = navController::navigateToEvents,
                    )

                    loginScreen(navController = navController) {
                        if (pageCount < routes.size - 1) pageCount++
                        navController.navigate(route = routes[pageCount]) {
                            popUpTo(route = LOGIN_ROUTE) { inclusive = true }
                        }
                    }

                    informationScreen(navController = navController) {
                        if (pageCount < routes.size - 1) pageCount++
                        navController.navigate(route = routes[pageCount]) {
                            popUpTo(route = INFORMATION_ROUTE) { inclusive = true }
                        }
                    }

                    settingsNavigation(
                        navController = navController,
                        navigateToLogin = navController::navigateToLogin,
                        navigateToInformation = navController::navigateToInformation,
                    )

                    feedbackDialog(navController = navController)

                    composable(route = "SPLASHSCREEN_SET_ROUTE") {
                        val context = LocalContext.current

                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(
                                        color = ContextCompat.getColor(
                                            context,
                                            color.ic_launcher_background,
                                        ),
                                    ),
                                )
                                .sharedElement(
                                    state = rememberSharedContentState(key = "Logo_background"),
                                    animatedVisibilityScope = this@composable,
                                ),
                        ) {
                            Spacer(modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}
