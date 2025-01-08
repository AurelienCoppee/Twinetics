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

package dev.coppee.aurelien.twinetics.core.datastore

import dev.coppee.aurelien.twinetics.core.datastore.test.testUserPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Rn3UserPreferencesDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: Rn3UserPreferencesDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = Rn3UserPreferencesDataSource(
            tmpFolder.testUserPreferencesDataStore(testScope),
        )
    }

    @Test
    fun accessibilityEmphasizedSwitchesFalseByDefault() = testScope.runTest {
        assertFalse(subject.userData.first().hasAccessibilityEmphasizedSwitchesEnabled)
    }

    @Test
    fun userAccessibilityEmphasizedSwitchesIsTrueWhenSet() = testScope.runTest {
        subject.setAccessibilityEmphasizedSwitchesPreference(true)
        assertTrue(subject.userData.first().hasAccessibilityEmphasizedSwitchesEnabled)
    }
}
