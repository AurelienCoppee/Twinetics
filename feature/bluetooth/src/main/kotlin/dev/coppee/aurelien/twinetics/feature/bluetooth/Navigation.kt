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

package dev.coppee.aurelien.twinetics.feature.bluetooth

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.coppee.aurelien.twinetics.core.designsystem.LocalNavAnimatedVisibilityScope

const val BLUETOOTH_ROUTE = "bluetooth"

fun NavController.navigateToBluetooth(navOptions: NavOptions? = null) =
    navigate(BLUETOOTH_ROUTE, navOptions)

fun NavGraphBuilder.bluetoothScreen(
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToRecord: () -> Unit,
    navigateToHistory: () -> Unit,
) {
    composable(route = BLUETOOTH_ROUTE) {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            BluetoothRoute(
                navController = navController,
                navigateToSettings = navigateToSettings,
                navigateToRecord = navigateToRecord,
                navigateToHistory = navigateToHistory,
            )
        }
    }
}
