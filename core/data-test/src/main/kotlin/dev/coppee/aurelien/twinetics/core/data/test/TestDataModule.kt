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

package dev.coppee.aurelien.twinetics.core.data.test

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.coppee.aurelien.twinetics.core.data.di.DataModule
import dev.coppee.aurelien.twinetics.core.data.repository.eventFeedData.EventFeedDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.eventFeedData.FirestoreEventFeedDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.friendData.FirestoreFriendsDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.friendData.FriendsDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.friendFeedData.FirestoreFriendFeedDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.friendFeedData.FriendFeedDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.linksRn3UrlData.FirestoreLinksRn3UrlDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.linksRn3UrlData.LinksRn3UrlDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.publicFeedData.FirestorePublicFeedDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.publicFeedData.PublicFeedDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.triagingData.FirestoreTriagingDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.triagingData.TriagingDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.userData.UserDataRepository
import dev.coppee.aurelien.twinetics.core.data.test.repository.FakeUserDataRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
internal interface TestDataModule {
    @Binds
    fun bindsUserDataRepository(
        userDataRepository: FakeUserDataRepository,
    ): UserDataRepository

    @Binds
    fun bindsFriendsDataRepository(
        friendsDataRepository: FirestoreFriendsDataRepository,
    ): FriendsDataRepository

    @Binds
    fun bindsEventFeedDataRepository(
        eventFeedDataRepository: FirestoreEventFeedDataRepository,
    ): EventFeedDataRepository

    @Binds
    fun bindsFriendFeedDataRepository(
        friendFeedDataRepository: FirestoreFriendFeedDataRepository,
    ): FriendFeedDataRepository

    @Binds
    fun bindsPublicFeedDataRepository(
        publicFeedDataRepository: FirestorePublicFeedDataRepository,
    ): PublicFeedDataRepository

    @Binds
    fun bindsTriagingDataRepository(
        triagingDataRepository: FirestoreTriagingDataRepository,
    ): TriagingDataRepository

    @Binds
    fun bindsLinksRn3UrlDataRepository(
        linksRn3UrlDataRepository: FirestoreLinksRn3UrlDataRepository,
    ): LinksRn3UrlDataRepository
}
