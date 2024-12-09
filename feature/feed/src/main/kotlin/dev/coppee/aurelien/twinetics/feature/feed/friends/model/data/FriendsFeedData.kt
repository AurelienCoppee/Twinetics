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

package dev.coppee.aurelien.twinetics.feature.feed.friends.model.data

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.coppee.aurelien.twinetics.core.data.model.FriendEntity
import dev.coppee.aurelien.twinetics.core.model.data.AddressInfo
import dev.coppee.aurelien.twinetics.core.user.Rn3User
import dev.coppee.aurelien.twinetics.feature.feed.model.PostEntity

data class FriendsFeedData(
    val user: Rn3User,
    val address: AddressInfo,
    val phone: PhoneNumber?,
    val friends: List<FriendEntity>,
    val posts: List<PostEntity>,
)
