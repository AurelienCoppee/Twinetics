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

package dev.coppee.aurelien.twinetics.feature.feed.publics

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import dev.coppee.aurelien.twinetics.core.designsystem.LocalNavAnimatedVisibilityScope

const val PUBLICFEED_ROUTE = "publicfeed"

fun NavController.navigateToPublicFeed(builder: NavOptionsBuilder.() -> Unit) =
    navigate(route = PUBLICFEED_ROUTE, builder = builder)

fun NavGraphBuilder.publicFeedScreen(
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToConnect: () -> Unit,
    navigateToFriends: () -> Unit,
    navigateToPublication: () -> Unit,
    navigateToEvents: () -> Unit,
) {
    composable(route = PUBLICFEED_ROUTE) {
        CompositionLocalProvider(value = LocalNavAnimatedVisibilityScope provides this) {
            PublicFeedRoute(
                navController = navController,
                navigateToSettings = navigateToSettings,
                navigateToConnect = navigateToConnect,
                navigateToFriends = navigateToFriends,
                navigateToPublication = navigateToPublication,
                navigateToEvents = navigateToEvents,
            )
        }
    }
}
