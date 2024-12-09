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

package dev.coppee.aurelien.twinetics.core.model.data

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber

data class UserData(
    val hasAccessibilityEmphasizedSwitchesEnabled: Boolean,
    val hasAccessibilityIconTooltipsEnabled: Boolean,
    val hasAccessibilityAltTextEnabled: Boolean,
    val hasTravelModeEnabled: Boolean,
    val hasFriendsMainEnabled: Boolean,
    val hasMetricsEnabled: Boolean,
    val hasCrashlyticsEnabled: Boolean,
    val shouldShowLoginScreenOnStartup: Boolean,
    val needInformation: Boolean,
    val isAppFirstLaunch: Boolean,
    val address: AddressInfo,
    val phone: PhoneNumber,
)
