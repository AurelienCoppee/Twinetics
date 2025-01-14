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

package dev.coppee.aurelien.twinetics.core.bluetooth.companion

import android.companion.AssociationInfo
import android.companion.CompanionDeviceManager
import android.os.Build
import androidx.annotation.RequiresApi
import dev.coppee.aurelien.twinetics.core.model.data.SensorData

fun CompanionDeviceManager.getAssociatedDevices(): List<SensorData> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            myAssociations.map { it.toAssociatedDevice() }
        } else {
            // Before Android 34 we can only get the MAC. We could use the BT adapter to find the
            // device, but to use CDM we only need the MAC.
            @Suppress("DEPRECATION")
            associations.map {
                SensorData(
                    id = -1,
                    address = it,
                    name = "",
                    device = null,
                )
            }
        }
    } else {
        emptyList()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal fun AssociationInfo.toAssociatedDevice() = SensorData(
    id = id,
    address = deviceMacAddress?.toString() ?: "N/A",
    name = displayName?.ifBlank { "N/A" }?.toString() ?: "N/A",
    device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        associatedDevice?.bleDevice?.device
    } else {
        null
    },
)