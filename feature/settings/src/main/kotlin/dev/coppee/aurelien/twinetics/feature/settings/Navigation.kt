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

package dev.coppee.aurelien.twinetics.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import dev.coppee.aurelien.twinetics.feature.settings.accessibility.accessibilityScreen
import dev.coppee.aurelien.twinetics.feature.settings.dataAndPrivacy.dataAndPrivacyScreen
import dev.coppee.aurelien.twinetics.feature.settings.developer.developerSettingsNavigation
import dev.coppee.aurelien.twinetics.feature.settings.main.SETTINGS_MAIN_ROUTE
import dev.coppee.aurelien.twinetics.feature.settings.main.mainScreen

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) =
    navigate(SETTINGS_ROUTE, navOptions)

fun NavGraphBuilder.settingsNavigation(
    navController: NavController,
    navigateToLogin: () -> Unit,
    navigateToInformation: () -> Unit,
) {
    navigation(startDestination = SETTINGS_MAIN_ROUTE, route = SETTINGS_ROUTE) {
        mainScreen(navController, navigateToLogin, navigateToInformation)
        accessibilityScreen(navController)
        dataAndPrivacyScreen(navController)
        developerSettingsNavigation(navController)
    }
}
