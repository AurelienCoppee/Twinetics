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

package dev.coppee.aurelien.twinetics.core.testing.repository

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.coppee.aurelien.twinetics.core.data.repository.userData.UserDataRepository
import dev.coppee.aurelien.twinetics.core.model.data.AddressInfo
import dev.coppee.aurelien.twinetics.core.model.data.UserData
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull

val emptyUserData = UserData(
    hasAccessibilityEmphasizedSwitchesEnabled = false,
    hasAccessibilityIconTooltipsEnabled = true,
    hasAccessibilityAltTextEnabled = false,
    hasTravelModeEnabled = false,
    hasFriendsMainEnabled = false,
    hasMetricsEnabled = true,
    hasCrashlyticsEnabled = true,
    shouldShowLoginScreenOnStartup = false,
    needInformation = false,
    isAppFirstLaunch = false,
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

class TestUserDataRepository : UserDataRepository {
    /**
     * The backing hot flow for the list of followed topic ids for testing.
     */
    private val _userData = MutableSharedFlow<UserData>(replay = 1, onBufferOverflow = DROP_OLDEST)

    private val currentUserData get() = _userData.replayCache.firstOrNull() ?: emptyUserData

    override val userData: Flow<UserData> = _userData.filterNotNull()

    override suspend fun setAccessibilityEmphasizedSwitches(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasAccessibilityEmphasizedSwitchesEnabled = value))
        }
    }

    override suspend fun setAccessibilityIconTooltips(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasAccessibilityIconTooltipsEnabled = value))
        }
    }

    override suspend fun setAccessibilityAltText(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasAccessibilityAltTextEnabled = value))
        }
    }

    override suspend fun setTravelMode(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasTravelModeEnabled = value))
        }
    }

    override suspend fun setFriendsMain(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasFriendsMainEnabled = value))
        }
    }

    override suspend fun setMetricsEnabled(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasMetricsEnabled = value))
        }
    }

    override suspend fun setCrashlyticsEnabled(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasCrashlyticsEnabled = value))
        }
    }

    override suspend fun setShouldShowLoginScreenOnStartup(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(shouldShowLoginScreenOnStartup = value))
        }
    }

    override suspend fun setNeedInformation(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(needInformation = value))
        }
    }

    override suspend fun setNotAppFirstLaunch() {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(isAppFirstLaunch = false))
        }
    }

    override suspend fun setAddressInfo(value: AddressInfo) {
        currentUserData.let { current ->
            _userData.tryEmit(
                current.copy(
                    address = AddressInfo(
                        country = null,
                        region = null,
                        locality = "",
                        postalCode = null,
                        street = "",
                        auxiliaryDetails = null,
                    ),
                ),
            )
        }
    }

    override suspend fun setPhoneNumber(value: PhoneNumber) {
        currentUserData.let { current ->
            _userData.tryEmit(
                current.copy(
                    phone = PhoneNumber(),
                ),
            )
        }
    }

    /**
     * A test-only API to allow setting of user data directly.
     */
    fun setUserData(userData: UserData) {
        _userData.tryEmit(userData)
    }
}
