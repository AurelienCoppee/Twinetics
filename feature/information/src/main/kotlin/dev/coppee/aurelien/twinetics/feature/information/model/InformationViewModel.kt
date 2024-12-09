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

package dev.coppee.aurelien.twinetics.feature.information.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.coppee.aurelien.twinetics.core.auth.AuthHelper
import dev.coppee.aurelien.twinetics.core.data.repository.userData.UserDataRepository
import dev.coppee.aurelien.twinetics.core.model.data.AddressInfo
import dev.coppee.aurelien.twinetics.feature.information.model.InformationUiState.Loading
import dev.coppee.aurelien.twinetics.feature.information.model.InformationUiState.Success
import dev.coppee.aurelien.twinetics.feature.information.model.data.InformationData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class InformationViewModel @Inject constructor(
    authHelper: AuthHelper,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    fun save(address: AddressInfo, phone: PhoneNumber?) {
        viewModelScope.launch {
            userDataRepository.setNeedInformation(false)
            userDataRepository.setAddressInfo(address)
            phone?.let { userDataRepository.setPhoneNumber(it) }
        }
    }

    val uiState: StateFlow<InformationUiState> =
        combine(
            userDataRepository.userData,
            authHelper.getUserFlow(),
        ) { userData, user ->
            Success(
                InformationData(
                    user = user,
                    address = userData.address,
                    phone = userData.phone,
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
