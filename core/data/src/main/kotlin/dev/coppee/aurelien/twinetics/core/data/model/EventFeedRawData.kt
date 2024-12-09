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

package dev.coppee.aurelien.twinetics.core.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

@Keep
data class EventFeedRawData(
    @DocumentId val uid: String? = null,
    val ownerUserUid: String = "RahNeil_N3:error:xTdZVv31n9S4fjOB0dFtJBk2ZZR6Ch5F",
    val userId: String? = null,
    val sharingScope: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val title: String? = null,
    val description: String? = null,
    val location: String? = null,
    val date: Timestamp? = null,
    val private: Boolean = true,
)
