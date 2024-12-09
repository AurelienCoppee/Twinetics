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

package dev.coppee.aurelien.twinetics.core.data.repository.friendData

import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.coppee.aurelien.twinetics.core.analytics.AnalyticsHelper
import dev.coppee.aurelien.twinetics.core.auth.AuthHelper
import dev.coppee.aurelien.twinetics.core.data.model.FriendRawData
import dev.coppee.aurelien.twinetics.core.data.repository.logAddFriend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class FirestoreFriendsDataRepository @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val authHelper: AuthHelper,
) : FriendsDataRepository {

    override val userFriends: Flow<List<FriendRawData>> =
        authHelper.getUserFlow().transformLatest { user ->
            emitAll(
                try {
                    Firebase.firestore
                        .collection("friends")
                        .whereEqualTo(
                            FriendRawData::ownerUserUid.name,
                            user.getUid(),
                        )
                        .dataObjects<FriendRawData>()
                } catch (e: Exception) {
                    throw e
                    // TODO Feedback error
                },
            )
        }

    override fun addFriend(friendRawData: FriendRawData) {
        Firebase.firestore
            .collection("friends")
            .add(
                friendRawData.copy(
                    ownerUserUid = authHelper.getUser().getUid(),
                ),
            )

        analyticsHelper.logAddFriend()
    }
}
