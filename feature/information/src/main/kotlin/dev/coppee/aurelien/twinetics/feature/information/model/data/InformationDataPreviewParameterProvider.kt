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

package dev.coppee.aurelien.twinetics.feature.information.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.coppee.aurelien.twinetics.core.model.data.AddressInfo
import dev.coppee.aurelien.twinetics.core.user.Rn3User.AnonymousUser
import dev.coppee.aurelien.twinetics.core.user.Rn3User.SignedInUser
import dev.coppee.aurelien.twinetics.feature.information.model.data.PreviewParameterData.informationData_default
import dev.coppee.aurelien.twinetics.feature.information.model.data.PreviewParameterData.informationData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [InformationData] for Composable previews.
 */
class FriendsFeedDataPreviewParameterProvider :
    PreviewParameterProvider<InformationData> {
    override val values: Sequence<InformationData> =
        sequenceOf(informationData_default).plus(informationData_mutations)
}

object PreviewParameterData {
    val informationData_default = InformationData(
        user = SignedInUser(
            uid = "androidPreviewID",
            displayName = "Android Preview",
            pfpUri = null,
            isAdmin = false,
            email = "androidPreview@rahmouni.dev",
        ),
        address = AddressInfo(
            country = null,
            region = null,
            locality = "",
            postalCode = null,
            street = "",
            auxiliaryDetails = null,
        ),
        phone = PhoneNumber(),
    )
    val informationData_mutations = with(informationData_default) {
        sequenceOf(
            copy(
                user = AnonymousUser(
                    uid = "androidPreviewID",
                ),
            ),
        )
    }
}
