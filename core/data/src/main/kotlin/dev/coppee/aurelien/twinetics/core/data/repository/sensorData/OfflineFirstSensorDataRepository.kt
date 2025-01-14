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

import android.annotation.SuppressLint
import android.companion.CompanionDeviceManager
import dev.coppee.aurelien.twinetics.core.bluetooth.companion.getAssociatedDevices
import dev.coppee.aurelien.twinetics.core.model.data.SensorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class OfflineFirstSensorDataRepository
@Inject constructor(
    @SuppressLint("NewApi") private val companionDeviceManager: CompanionDeviceManager,
) : SensorDataRepository {

    override val sensorsFlow: Flow<List<SensorData>> = flow {
        emit(companionDeviceManager.getAssociatedDevices())
    }
}