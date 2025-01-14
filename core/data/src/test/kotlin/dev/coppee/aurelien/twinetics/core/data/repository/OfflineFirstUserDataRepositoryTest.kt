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
package dev.coppee.aurelien.twinetics.core.data.repository

import dev.coppee.aurelien.twinetics.core.analytics.NoOpAnalyticsHelper
import dev.coppee.aurelien.twinetics.core.data.repository.userData.OfflineFirstUserDataRepository
import dev.coppee.aurelien.twinetics.core.datastore.Rn3UserPreferencesDataSource
import dev.coppee.aurelien.twinetics.core.datastore.test.testUserPreferencesDataStore
import dev.coppee.aurelien.twinetics.core.model.data.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

class OfflineFirstUserDataRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private lateinit var subject: OfflineFirstUserDataRepository
    private lateinit var rn3UserPreferencesDataSource: Rn3UserPreferencesDataSource
    private val analyticsHelper = NoOpAnalyticsHelper()

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        rn3UserPreferencesDataSource = Rn3UserPreferencesDataSource(
            userPreferences = tmpFolder.testUserPreferencesDataStore(testScope),
        )
        subject = OfflineFirstUserDataRepository(
            rn3UserPreferencesDataSource = rn3UserPreferencesDataSource,
            analyticsHelper = analyticsHelper,
        )
    }

    @Test
    fun offlineFirstUserDataRepository_default_user_data_is_correct() =
        testScope.runTest {
            assertEquals(
                UserData(
                    hasAccessibilityEmphasizedSwitchesEnabled = false,
                    hasAccessibilityIconTooltipsEnabled = true,
                    hasMetricsEnabled = true,
                    hasCrashlyticsEnabled = true,
                    shouldShowLoginScreenOnStartup = true,
                    isAppFirstLaunch = true,
                ),
                subject.userFlow.first(),
            )
        }
}