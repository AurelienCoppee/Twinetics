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

package dev.coppee.aurelien.twinetics.feature.feed.publics.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.coppee.aurelien.twinetics.core.auth.AuthHelper
import dev.coppee.aurelien.twinetics.core.data.model.toEntity
import dev.coppee.aurelien.twinetics.core.data.repository.friendData.FriendsDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.publicFeedData.PublicFeedDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.userData.UserDataRepository
import dev.coppee.aurelien.twinetics.feature.feed.model.toEntity
import dev.coppee.aurelien.twinetics.feature.feed.publics.model.PublicFeedUiState.Loading
import dev.coppee.aurelien.twinetics.feature.feed.publics.model.PublicFeedUiState.Success
import dev.coppee.aurelien.twinetics.feature.feed.publics.model.data.PublicFeedData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PublicFeedViewModel @Inject constructor(
    authHelper: AuthHelper,
    userDataRepository: UserDataRepository,
    private val friendsDataRepository: FriendsDataRepository,
    private val publicFeedDataRepository: PublicFeedDataRepository,
) : ViewModel() {

    val uiState: StateFlow<PublicFeedUiState> =
        combine(
            userDataRepository.userData,
            friendsDataRepository.userFriends,
            publicFeedDataRepository.userPublicPosts,
            authHelper.getUserFlow(),
        ) { userData, friends, posts, user ->
            Success(
                PublicFeedData(
                    user = user,
                    address = userData.address,
                    phone = userData.phone,
                    friends = friends.map { it.toEntity() },
                    posts = posts.sortedByDescending { it.timestamp }.map { it.toEntity() },
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
