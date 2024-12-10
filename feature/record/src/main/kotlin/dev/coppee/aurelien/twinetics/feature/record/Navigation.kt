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

package dev.coppee.aurelien.twinetics.feature.record

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.coppee.aurelien.twinetics.core.designsystem.LocalNavAnimatedVisibilityScope

const val RECORD_ROUTE = "record"

fun NavController.navigateToRecord(navOptions: NavOptions? = null) =
    navigate(route = RECORD_ROUTE, navOptions = navOptions)

fun NavGraphBuilder.recordScreen(
) {
    composable(route = RECORD_ROUTE) {
        CompositionLocalProvider(value = LocalNavAnimatedVisibilityScope provides this) {
            RecordRoute(
            )
        }
    }
}
