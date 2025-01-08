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

package dev.coppee.aurelien.twinetics.feature.settings.dataAndPrivacy.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.coppee.aurelien.twinetics.core.data.repository.userData.UserDataRepository
import dev.coppee.aurelien.twinetics.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState.Loading
import dev.coppee.aurelien.twinetics.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState.Success
import dev.coppee.aurelien.twinetics.feature.settings.dataAndPrivacy.model.data.DataAndPrivacySettingsData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DataAndPrivacySettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<DataAndPrivacySettingsUiState> =
        userDataRepository.userFlow
            .map { userData ->
                Success(
                    dataAndPrivacySettingsData = DataAndPrivacySettingsData(
                        hasMetricsEnabled = userData.hasMetricsEnabled,
                        hasCrashlyticsEnabled = userData.hasCrashlyticsEnabled,
                    ),
                )
            }.stateIn(
                scope = viewModelScope,
                initialValue = Loading,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            )

    fun setMetricsEnabled(value: Boolean) = viewModelScope.launch {
        userDataRepository.setMetricsEnabled(value)
    }

    fun setCrashlyticsEnabled(value: Boolean) = viewModelScope.launch {
        userDataRepository.setCrashlyticsEnabled(value)
    }
}
