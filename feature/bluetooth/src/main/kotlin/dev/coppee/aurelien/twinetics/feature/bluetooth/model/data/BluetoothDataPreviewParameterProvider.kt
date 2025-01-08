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

package dev.coppee.aurelien.twinetics.feature.bluetooth.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.coppee.aurelien.twinetics.core.model.data.SensorData
import dev.coppee.aurelien.twinetics.core.user.Rn3User.AnonymousUser
import dev.coppee.aurelien.twinetics.core.user.Rn3User.SignedInUser
import dev.coppee.aurelien.twinetics.feature.bluetooth.model.data.PreviewParameterData.bluetoothData_default
import dev.coppee.aurelien.twinetics.feature.bluetooth.model.data.PreviewParameterData.bluetoothData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [BluetoothData] for Composable previews.
 */
class BluetoothDataPreviewParameterProvider :
    PreviewParameterProvider<BluetoothData> {
    override val values: Sequence<BluetoothData> =
        sequenceOf(bluetoothData_default).plus(bluetoothData_mutations)
}

object PreviewParameterData {
    val bluetoothData_default = BluetoothData(
        user = SignedInUser(
            uid = "androidPreviewID",
            displayName = "Android Preview",
            pfpUri = null,
            isAdmin = false,
            email = "androidPreview@rahmouni.dev",
        ),
        sensors = listOf(
            SensorData(
                address = "",
                name = "Heart Rate Monitor",
                type = "",
            )
        )
    )
    val bluetoothData_mutations = with(bluetoothData_default) {
        sequenceOf(
            copy(
                user = AnonymousUser(
                    uid = "androidPreviewID",
                ),
            ),
        )
    }
}
