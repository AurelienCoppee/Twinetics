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

package dev.coppee.aurelien.twinetics.feature.settings.main.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.coppee.aurelien.twinetics.core.user.Rn3User.AnonymousUser
import dev.coppee.aurelien.twinetics.core.user.Rn3User.SignedInUser
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.InAppUpdateState.NoUpdateAvailable
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.InAppUpdateState.UpdateAvailable
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.PreviewParameterData.settingsData_default
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.PreviewParameterData.settingsData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [SettingsData] for Composable previews.
 */
class SettingsDataPreviewParameterProvider :
    PreviewParameterProvider<SettingsData> {
    override val values: Sequence<SettingsData> =
        sequenceOf(settingsData_default).plus(settingsData_mutations)
}

object PreviewParameterData {
    val settingsData_default = SettingsData(
        user = SignedInUser(
            uid = "androidPreviewID",
            displayName = "Android Preview",
            pfpUri = null,
            isAdmin = false,
            email = "androidPreview@rahmouni.dev",
        ),
        devSettingsEnabled = false,
        inAppUpdateData = NoUpdateAvailable,
    )
    val settingsData_mutations = with(settingsData_default) {
        sequenceOf(
            copy(
                user = AnonymousUser(
                    uid = "androidPreviewID",
                ),
            ),
            copy(devSettingsEnabled = true),
            copy(inAppUpdateData = UpdateAvailable(null, null)),
        )
    }
}
