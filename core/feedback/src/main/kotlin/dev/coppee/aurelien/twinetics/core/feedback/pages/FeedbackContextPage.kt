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

package dev.coppee.aurelien.twinetics.core.feedback.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3SurfaceDefaults
import dev.coppee.aurelien.twinetics.core.designsystem.component.getHaptic
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileSwitch
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.padding
import dev.coppee.aurelien.twinetics.core.feedback.FeedbackMessages
import dev.coppee.aurelien.twinetics.core.feedback.R.string

@Composable
internal fun FeedbackContextPage(
    bug: Boolean,
    hasContext: Boolean,
    onCurrentPage: Boolean,
    sendScreenshot: Boolean,
    sendAdditionalInfo: Boolean,
    nextPage: (Boolean, Boolean, Boolean) -> Unit,
    previousPage: (Boolean, Boolean, Boolean) -> Unit,
) {
    val haptic = getHaptic()

    var onCurrentPageValue by rememberSaveable { mutableStateOf(onCurrentPage) }
    var sendScreenshotValue by rememberSaveable { mutableStateOf(sendScreenshot) }
    var sendAdditionalInfoValue by rememberSaveable { mutableStateOf(sendAdditionalInfo) }

    Column {
        Spacer(modifier = Modifier.height(8.dp))

        FeedbackMessages(
            messages = listOf(stringResource(string.core_feedback_contextPage_contextMessage)),
            paddingValues = Rn3SurfaceDefaults.paddingValues.copy(bottom = 0.dp),
        )

        if (hasContext) {
            Rn3TileSwitch(
                title = if (bug) {
                    stringResource(string.core_feedback_contextPage_bugPageTile_title)
                } else {
                    stringResource(
                        string.core_feedback_contextPage_suggestionPageTile_title,
                    )
                },
                icon = Icons.Outlined.LocationOn,
                checked = onCurrentPageValue,
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                onCurrentPageValue = it
            }
        }
        if (bug) {
            // TODO replace `enabled = false` by `enabled = onCurrentPageValue` once that #462 is done
            Rn3TileSwitch(
                title = stringResource(string.core_feedback_contextPage_screenshotTile_title),
                icon = Icons.Outlined.Screenshot,
                checked = onCurrentPageValue && sendScreenshotValue,
                enabled = false,
                supportingText = stringResource(string.core_feedback_contextPage_screenshotTile_supportingText),
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                sendScreenshotValue = it
            }
        }
        Rn3TileSwitch(
            title = stringResource(string.core_feedback_contextPage_additionalInfoTile_title),
            icon = Icons.Outlined.Android,
            checked = sendAdditionalInfoValue,
            supportingText = stringResource(string.core_feedback_contextPage_additionalInfoTile_supportingText),
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        ) {
            sendAdditionalInfoValue = it
        }

        Row(
            Modifier.padding(Rn3SurfaceDefaults.paddingValues.copy(top = 0.dp, bottom = 12.dp)),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilledTonalButton(
                onClick = {
                    haptic.click()
                    previousPage(
                        onCurrentPageValue,
                        onCurrentPageValue && sendScreenshotValue,
                        sendAdditionalInfoValue,
                    )
                },
            ) {
                Text(text = stringResource(string.core_feedback_backButton_title))
            }
            Button(
                onClick = {
                    haptic.click()
                    nextPage(
                        onCurrentPageValue,
                        bug && onCurrentPageValue && sendScreenshotValue,
                        sendAdditionalInfoValue,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(string.core_feedback_continueButton_title))
            }
        }
    }
}
