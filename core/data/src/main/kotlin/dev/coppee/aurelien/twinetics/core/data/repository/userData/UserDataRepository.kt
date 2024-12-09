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

package dev.coppee.aurelien.twinetics.core.data.repository.userData

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.coppee.aurelien.twinetics.core.model.data.AddressInfo
import dev.coppee.aurelien.twinetics.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets whether the user has enabled the accessibility settings that emphasizes selected switches
     */
    suspend fun setAccessibilityEmphasizedSwitches(value: Boolean)

    /**
     * Sets whether the user has enabled the accessibility settings that adds tooltips to Icons
     */
    suspend fun setAccessibilityIconTooltips(value: Boolean)

    /**
     * Sets whether the user has enabled the accessibility settings that show alt texts
     */
    suspend fun setAccessibilityAltText(value: Boolean)

    suspend fun setTravelMode(value: Boolean)

    suspend fun setFriendsMain(value: Boolean)

    suspend fun setMetricsEnabled(value: Boolean)
    suspend fun setCrashlyticsEnabled(value: Boolean)
    suspend fun setShouldShowLoginScreenOnStartup(value: Boolean)
    suspend fun setNeedInformation(value: Boolean)
    suspend fun setNotAppFirstLaunch()
    suspend fun setAddressInfo(value: AddressInfo)
    suspend fun setPhoneNumber(value: PhoneNumber)
}
