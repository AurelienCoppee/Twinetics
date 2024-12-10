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

package dev.coppee.aurelien.twinetics.feature.record

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.coppee.aurelien.twinetics.core.ui.TrackScreenViewEvent
import dev.coppee.aurelien.twinetics.feature.record.model.RecordUiState.Loading
import dev.coppee.aurelien.twinetics.feature.record.model.RecordUiState.Success
import dev.coppee.aurelien.twinetics.feature.record.model.RecordViewModel

@Composable
internal fun RecordRoute(
    viewModel: RecordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        Loading -> {}
        is Success -> PublicationScreen(
        )
    }

    TrackScreenViewEvent(screenName = "Publication")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun PublicationScreen(
) {

}
