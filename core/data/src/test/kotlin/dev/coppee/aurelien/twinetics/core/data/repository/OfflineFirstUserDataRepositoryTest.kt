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

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.coppee.aurelien.twinetics.core.analytics.NoOpAnalyticsHelper
import dev.coppee.aurelien.twinetics.core.data.repository.userData.OfflineFirstUserDataRepository
import dev.coppee.aurelien.twinetics.core.datastore.Rn3PreferencesDataSource
import dev.coppee.aurelien.twinetics.core.datastore.test.testUserPreferencesDataStore
import dev.coppee.aurelien.twinetics.core.model.data.AddressInfo
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

    private lateinit var rn3PreferencesDataSource: Rn3PreferencesDataSource

    private val analyticsHelper = NoOpAnalyticsHelper()

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        rn3PreferencesDataSource = Rn3PreferencesDataSource(
            userPreferences = tmpFolder.testUserPreferencesDataStore(testScope),
        )

        subject = OfflineFirstUserDataRepository(
            rn3PreferencesDataSource = rn3PreferencesDataSource,
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
                    hasAccessibilityAltTextEnabled = false,
                    hasTravelModeEnabled = false,
                    hasFriendsMainEnabled = false,
                    hasMetricsEnabled = true,
                    hasCrashlyticsEnabled = true,
                    shouldShowLoginScreenOnStartup = true,
                    needInformation = false,
                    isAppFirstLaunch = true,
                    address = AddressInfo(
                        country = null,
                        region = null,
                        locality = "",
                        postalCode = null,
                        street = "",
                        auxiliaryDetails = null,
                    ),
                    phone = PhoneNumber(),
                ),
                subject.userData.first(),
            )
        }
}
