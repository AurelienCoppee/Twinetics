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

package dev.coppee.aurelien.twinetics.feature.events.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.coppee.aurelien.twinetics.core.auth.AuthHelper
import dev.coppee.aurelien.twinetics.core.data.model.toEntity
import dev.coppee.aurelien.twinetics.core.data.repository.eventFeedData.EventFeedDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.friendData.FriendsDataRepository
import dev.coppee.aurelien.twinetics.feature.events.model.EventsUiState.Loading
import dev.coppee.aurelien.twinetics.feature.events.model.EventsUiState.Success
import dev.coppee.aurelien.twinetics.feature.events.model.data.EventsData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class EventsViewModel @Inject constructor(
    authHelper: AuthHelper,
    private val friendsDataRepository: FriendsDataRepository,
    private val eventFeedDataRepository: EventFeedDataRepository,
) : ViewModel() {

    val uiState: StateFlow<EventsUiState> =
        combine(
            friendsDataRepository.userFriends,
            eventFeedDataRepository.userEventPosts,
            authHelper.getUserFlow(),
        ) { friends, posts, user ->
            Success(
                EventsData(
                    user = user,
                    friends = friends.map { it.toEntity() },
                    posts = posts.map { it.toEntity() },
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
