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

package dev.coppee.aurelien.twinetics.feature.bluetooth.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.coppee.aurelien.twinetics.core.auth.AuthHelper
import dev.coppee.aurelien.twinetics.feature.bluetooth.model.BluetoothUiState.Loading
import dev.coppee.aurelien.twinetics.feature.bluetooth.model.BluetoothUiState.Success
import dev.coppee.aurelien.twinetics.feature.bluetooth.model.data.BluetoothData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    authHelper: AuthHelper,
) : ViewModel() {

    val uiState: StateFlow<BluetoothUiState> =
        authHelper.getUserFlow()
            .map { user ->
                Success(BluetoothData(user = user))
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = Loading,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            )
}
