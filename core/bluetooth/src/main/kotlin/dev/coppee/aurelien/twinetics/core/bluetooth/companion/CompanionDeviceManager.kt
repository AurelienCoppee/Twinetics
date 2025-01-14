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

import android.bluetooth.le.ScanResult
import android.companion.AssociationInfo
import android.companion.AssociationRequest
import android.companion.CompanionDeviceManager
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import dev.coppee.aurelien.twinetics.core.model.data.SensorData
import kotlinx.coroutines.CompletableDeferred
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.O)
fun Intent.getAssociationResult(): SensorData? {
    var result: SensorData? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        result = getParcelableExtra(
            CompanionDeviceManager.EXTRA_ASSOCIATION,
            AssociationInfo::class.java,
        )?.toAssociatedDevice()
    } else {
        // Below Android 33 the result returns either a BLE ScanResult, a
        // Classic BluetoothDevice or a Wifi ScanResult
        // In our case we are looking for our BLE GATT server so we can cast directly
        // to the BLE ScanResult
        @Suppress("DEPRECATION")
        val scanResult = getParcelableExtra<ScanResult>(CompanionDeviceManager.EXTRA_DEVICE)
        if (scanResult != null) {
            result = SensorData(
                id = scanResult.advertisingSid,
                address = scanResult.device.address ?: "N/A",
                name = scanResult.scanRecord?.deviceName ?: "N/A",
                device = scanResult.device,
            )
        }
    }
    return result
}


@RequiresApi(Build.VERSION_CODES.O)
suspend fun requestDeviceAssociation(
    deviceManager: CompanionDeviceManager,
    selectDeviceLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
): IntentSender {
    val pairingRequest: AssociationRequest = AssociationRequest.Builder()
        .setSingleDevice(false)
        .build()

    val result = CompletableDeferred<IntentSender>()

    val callback = object : CompanionDeviceManager.Callback() {
        override fun onAssociationPending(intentSender: IntentSender) {
            selectDeviceLauncher.launch(
                IntentSenderRequest.Builder(intentSender).build(),
            )
        }

        @Suppress("OVERRIDE_DEPRECATION")
        override fun onDeviceFound(intentSender: IntentSender) {
            result.complete(intentSender)
        }

        override fun onAssociationCreated(associationInfo: AssociationInfo) {
            // This callback was added in API 33 but the result is also send in the activity result.
            // For handling backwards compatibility we can just have all the logic there instead
        }

        override fun onFailure(errorMessage: CharSequence?) {
            result.completeExceptionally(IllegalStateException(errorMessage?.toString().orEmpty()))
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val executor = Executor { it.run() }
        deviceManager.associate(pairingRequest, executor, callback)
    } else {
        deviceManager.associate(pairingRequest, callback, null)
    }
    return result.await()
}