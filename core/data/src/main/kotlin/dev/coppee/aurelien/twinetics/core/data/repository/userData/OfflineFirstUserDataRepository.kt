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

import dev.coppee.aurelien.twinetics.core.analytics.AnalyticsHelper
import dev.coppee.aurelien.twinetics.core.data.repository.logAccessibilityEmphasizedSwitchesPreferenceChanged
import dev.coppee.aurelien.twinetics.core.data.repository.logAccessibilityIconTooltipsPreferenceChanged
import dev.coppee.aurelien.twinetics.core.data.repository.logCrashlyticsPreferenceChanged
import dev.coppee.aurelien.twinetics.core.data.repository.logMetricsPreferenceChanged
import dev.coppee.aurelien.twinetics.core.datastore.Rn3UserPreferencesDataSource
import dev.coppee.aurelien.twinetics.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstUserDataRepository @Inject constructor(
    private val rn3UserPreferencesDataSource: Rn3UserPreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {

    override val userFlow: Flow<UserData> = rn3UserPreferencesDataSource.userData

    override suspend fun setAccessibilityEmphasizedSwitches(value: Boolean) {
        rn3UserPreferencesDataSource.setAccessibilityEmphasizedSwitchesPreference(value)
        analyticsHelper.logAccessibilityEmphasizedSwitchesPreferenceChanged(value)
    }

    override suspend fun setAccessibilityIconTooltips(value: Boolean) {
        rn3UserPreferencesDataSource.setAccessibilityIconTooltipsPreference(value)
        analyticsHelper.logAccessibilityIconTooltipsPreferenceChanged(value)
    }

    override suspend fun setMetricsEnabled(value: Boolean) {
        rn3UserPreferencesDataSource.setMetricsEnabledPreference(value)
        analyticsHelper.logMetricsPreferenceChanged(value)
    }
    override suspend fun setCrashlyticsEnabled(value: Boolean) {
        rn3UserPreferencesDataSource.setCrashlyticsEnabledPreference(value)
        analyticsHelper.logCrashlyticsPreferenceChanged(value)
    }

    override suspend fun setShouldShowLoginScreenOnStartup(value: Boolean) =
        rn3UserPreferencesDataSource.setShouldShowLoginScreenOnStartup(value)

    override suspend fun setNotAppFirstLaunch() = rn3UserPreferencesDataSource.setNotAppFirstLaunch()
}