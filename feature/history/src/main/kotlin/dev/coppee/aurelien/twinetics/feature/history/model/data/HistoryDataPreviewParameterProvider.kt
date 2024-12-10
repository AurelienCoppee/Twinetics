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

package dev.coppee.aurelien.twinetics.feature.history.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.coppee.aurelien.twinetics.core.user.Rn3User.AnonymousUser
import dev.coppee.aurelien.twinetics.core.user.Rn3User.SignedInUser
import dev.coppee.aurelien.twinetics.feature.history.model.data.PreviewParameterData.historyData_default
import dev.coppee.aurelien.twinetics.feature.history.model.data.PreviewParameterData.eventsData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [HistoryData] for Composable previews.
 */
class EventsDataPreviewParameterProvider :
    PreviewParameterProvider<HistoryData> {
    override val values: Sequence<HistoryData> =
        sequenceOf(historyData_default).plus(eventsData_mutations)
}

object PreviewParameterData {
    val historyData_default = HistoryData(
        user = SignedInUser(
            uid = "androidPreviewID",
            displayName = "Android Preview",
            pfpUri = null,
            isAdmin = false,
            email = "androidPreview@rahmouni.dev",
        ),
    )
    val eventsData_mutations = with(historyData_default) {
        sequenceOf(
            copy(
                user = AnonymousUser(
                    uid = "androidPreviewID",
                ),
            ),
        )
    }
}
