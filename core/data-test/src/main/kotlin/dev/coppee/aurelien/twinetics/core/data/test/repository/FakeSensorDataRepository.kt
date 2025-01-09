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

package dev.coppee.aurelien.twinetics.core.data.test.repository

import dev.coppee.aurelien.twinetics.core.data.repository.sensorData.SensorDataRepository
import dev.coppee.aurelien.twinetics.core.datastore.Rn3SensorDataSource
import dev.coppee.aurelien.twinetics.core.model.data.SensorData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Fake implementation of the [SensorDataRepository] that returns hardcoded user data.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeSensorDataRepository @Inject constructor(
    private val rn3SensorDataSource: Rn3SensorDataSource,
) : SensorDataRepository {

    override val sensorsFlow: Flow<List<SensorData>> =
        rn3SensorDataSource.sensorsFlow

    override suspend fun addSensor(sensor: SensorData) {
        rn3SensorDataSource.addSensor(sensor)
    }

    override suspend fun removeSensor(address: String) {
        rn3SensorDataSource.removeSensorByAddress(address)
    }
}
