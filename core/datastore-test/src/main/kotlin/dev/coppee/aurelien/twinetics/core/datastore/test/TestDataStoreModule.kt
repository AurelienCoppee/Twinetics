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

package dev.coppee.aurelien.twinetics.core.datastore.test

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import coppee.aurelien.twinetics.core.datastore.SensorList
import coppee.aurelien.twinetics.core.datastore.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.coppee.aurelien.twinetics.core.datastore.SensorSerializer
import dev.coppee.aurelien.twinetics.core.datastore.UserPreferencesSerializer
import dev.coppee.aurelien.twinetics.core.datastore.di.DataStoreModule
import dev.coppee.aurelien.twinetics.core.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
internal object TestDataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationScope scope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<UserPreferences> =
        tmpFolder.testUserPreferencesDataStore(
            coroutineScope = scope,
            userPreferencesSerializer = userPreferencesSerializer,
        )

    @Provides
    @Singleton
    fun providesSensorDataStore(
        @ApplicationScope scope: CoroutineScope,
        sensorSerializer: SensorSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<SensorList> =
        tmpFolder.testSensorDataStore(
            coroutineScope = scope,
            sensorSerializer = sensorSerializer,
        )
}

fun TemporaryFolder.testUserPreferencesDataStore(
    coroutineScope: CoroutineScope,
    userPreferencesSerializer: UserPreferencesSerializer = UserPreferencesSerializer(),
) = DataStoreFactory.create(
    serializer = userPreferencesSerializer,
    scope = coroutineScope,
) {
    newFile("RahNeil_N3:user_preferences_test.pb")
}

fun TemporaryFolder.testSensorDataStore(
    coroutineScope: CoroutineScope,
    sensorSerializer: SensorSerializer = SensorSerializer(),
) = DataStoreFactory.create(
    serializer = sensorSerializer,
    scope = coroutineScope,
) {
    newFile("RahNeil_N3:sensor_test.pb")
}
