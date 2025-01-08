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

package dev.coppee.aurelien.twinetics.core.data.repository.userData

import dev.coppee.aurelien.twinetics.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    /**
     * Stream of [UserData]
     */
    val userFlow: Flow<UserData>

    /**
     * Sets whether the user has enabled the accessibility settings that emphasizes selected switches
     */
    suspend fun setAccessibilityEmphasizedSwitches(value: Boolean)

    /**
     * Sets whether the user has enabled the accessibility settings that adds tooltips to Icons
     */
    suspend fun setAccessibilityIconTooltips(value: Boolean)

    /**
     * Enables or disables the collection of analytics metrics to monitor app performance and user engagement.
     */
    suspend fun setMetricsEnabled(value: Boolean)

    /**
     * Enables or disables integration with Crashlytics to report unexpected crashes for improving app stability.
     */
    suspend fun setCrashlyticsEnabled(value: Boolean)

    /**
     * Decides whether to show the login screen on startup to manage user access and session control.
     */
    suspend fun setShouldShowLoginScreenOnStartup(value: Boolean)

    /**
     * Marks the app as not being launched for the first time, to customize the initial user experience.
     */
    suspend fun setNotAppFirstLaunch()
}