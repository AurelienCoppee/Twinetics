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

import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.coppee.aurelien.twinetics.core.common.Rn3Uri
import dev.coppee.aurelien.twinetics.core.common.Rn3Uri.AndroidPreview
import dev.coppee.aurelien.twinetics.core.common.Rn3Uri.Available
import dev.coppee.aurelien.twinetics.core.common.Rn3Uri.InMaintenance
import dev.coppee.aurelien.twinetics.core.common.Rn3Uri.SoonAvailable
import dev.coppee.aurelien.twinetics.core.common.Rn3Uri.Unavailable
import dev.coppee.aurelien.twinetics.core.common.openUri
import dev.coppee.aurelien.twinetics.core.common.prepareToOpenUri
import dev.coppee.aurelien.twinetics.core.designsystem.R.string
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewComponentDefault
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewComponentVariation
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3Theme

@Composable
fun Rn3TileUri(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    uri: Rn3Uri,
    supportingText: String? = null,
    forceSupportingText: Boolean = false,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        context.prepareToOpenUri(uri)
    }

    Rn3TileClick(
        modifier = modifier,
        title = title,
        icon = icon,
        supportingText = if (forceSupportingText) {
            supportingText
        } else {
            when (uri) {
                is AndroidPreview -> supportingText
                is InMaintenance -> stringResource(string.core_designsystem_tileUri_inMaintenance_supportingText)
                is Unavailable -> stringResource(string.core_designsystem_tileUri_unavailable_supportingText)
                is SoonAvailable -> stringResource(string.core_designsystem_tileUri_soonAvailable_supportingText)
                is Available -> supportingText
            }
        },
        external = true,
        enabled = uri is AndroidPreview || uri is Available,
    ) {
        context.openUri(uri)
    }
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            Rn3TileUri(
                title = "Title",
                icon = Outlined.EmojiEvents,
                uri = AndroidPreview,
            )
        }
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun InMaintenance() {
    Rn3Theme {
        Surface {
            Rn3TileUri(
                title = "Title",
                icon = Outlined.EmojiEvents,
                uri = InMaintenance,
            )
        }
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun Unavailable() {
    Rn3Theme {
        Surface {
            Rn3TileUri(
                title = "Title",
                icon = Outlined.EmojiEvents,
                uri = Unavailable,
            )
        }
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun SoonAvailable() {
    Rn3Theme {
        Surface {
            Rn3TileUri(
                title = "Title",
                icon = Outlined.EmojiEvents,
                uri = SoonAvailable,
            )
        }
    }
}
