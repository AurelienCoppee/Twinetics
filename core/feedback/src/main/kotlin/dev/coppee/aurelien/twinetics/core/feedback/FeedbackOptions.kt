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

package dev.coppee.aurelien.twinetics.core.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3SurfaceDefaults
import dev.coppee.aurelien.twinetics.core.designsystem.component.getHaptic
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.padding

@Composable
internal fun FeedbackOptions(
    options: Map<String, String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    val haptic = getHaptic()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Rn3SurfaceDefaults.paddingValues),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        options.forEach { (key, title) ->
            ExtendedFloatingActionButton(
                text = { Text(text = title, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                icon = {
                    AnimatedVisibility(
                        visible = selectedOption == key,
                        enter = fadeIn() + scaleIn() + expandHorizontally(),
                        exit = fadeOut() + scaleOut() + shrinkHorizontally(),
                    ) {
                        Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
                    }
                },
                onClick = {
                    haptic.click()
                    onOptionSelected(key)
                },
                modifier = Modifier
                    .weight(1f),
                containerColor = if (key == selectedOption) FloatingActionButtonDefaults.containerColor else MaterialTheme.colorScheme.surfaceVariant,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            )
        }
    }
}
