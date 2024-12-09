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

package dev.coppee.aurelien.twinetics.core.designsystem.component.tile

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewComponentDefault
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3Theme
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3TextDefaults
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValues
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.padding

@Composable
fun Rn3TileSmallHeader(
    modifier: Modifier = Modifier,
    title: String,
    paddingValues: Rn3PaddingValues = Rn3TextDefaults.paddingValues,
) {
    Text(
        text = title,
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            Rn3TileSmallHeader(title = "General")
        }
    }
}
