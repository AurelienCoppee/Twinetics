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

package dev.coppee.aurelien.twinetics.core.data.repository.sensorData

import dev.coppee.aurelien.twinetics.core.analytics.AnalyticsHelper
import dev.coppee.aurelien.twinetics.core.data.repository.logSensorAdded
import dev.coppee.aurelien.twinetics.core.data.repository.logSensorRemoved
import dev.coppee.aurelien.twinetics.core.datastore.Rn3SensorDataSource
import dev.coppee.aurelien.twinetics.core.model.data.SensorData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstSensorDataRepository @Inject constructor(
    private val rn3SensorDataSource: Rn3SensorDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : SensorDataRepository {

    override val sensorsFlow: Flow<List<SensorData>> = rn3SensorDataSource.sensorsFlow

    override suspend fun addSensor(sensor: SensorData) {
        rn3SensorDataSource.addSensor(sensor)
        analyticsHelper.logSensorAdded(sensor)
    }

    override suspend fun removeSensor(address: String) {
        rn3SensorDataSource.removeSensorByAddress(address)
        analyticsHelper.logSensorRemoved(address)
    }
}