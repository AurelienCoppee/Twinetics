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

import androidx.datastore.core.DataStore
import coppee.aurelien.twinetics.core.datastore.SensorList
import dev.coppee.aurelien.twinetics.core.model.data.SensorData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Rn3SensorDataSource @Inject constructor(
    private val sensorList: DataStore<SensorList>,
) {
    val sensorsFlow = sensorList.data.map { sensors ->
        sensors.devicesList.map { sensor ->
            SensorData(
                address = sensor.address,
                name = sensor.name,
                type = sensor.type,
            )
        }
    }

    suspend fun addSensor(sensor: SensorData) {
    }

    suspend fun removeSensorByAddress(address: String) {
    }

    suspend fun updateSensorIsActive(address: String, newActive: Boolean) {
    }
}
