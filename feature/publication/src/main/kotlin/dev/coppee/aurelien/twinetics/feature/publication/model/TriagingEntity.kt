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

package dev.coppee.aurelien.twinetics.feature.publication.model

import dev.coppee.aurelien.twinetics.core.data.model.TriagingRawData
import dev.coppee.aurelien.twinetics.core.designsystem.rebased.PostType
import dev.coppee.aurelien.twinetics.core.designsystem.rebased.SharingScope

data class TriagingEntity(
    val uid: String? = null,
    val text: String,
    val analysed: Boolean,
    val feed: FeedType?,
    val scope: SharingScope?,
    var analyse: AnalyseType?,
    val type: PostType?,
)

fun TriagingRawData.toEntity(): TriagingEntity {
    if (uid == null) throw IllegalStateException("Attempted to convert a TriagingRawData with null uid to a triagingEntity")

    return TriagingEntity(
        uid = uid,
        text = text ?: "",
        analysed = analysed,
        feed = if (feed.isNullOrBlank()) null else FeedType.fromString(feed),
        scope = if (scope.isNullOrBlank()) null else SharingScope.fromString(scope),
        analyse = if (analyse.isNullOrBlank()) null else AnalyseType.fromString(analyse),
        type = if (type.isNullOrBlank()) null else PostType.fromString(type),
    )
}

