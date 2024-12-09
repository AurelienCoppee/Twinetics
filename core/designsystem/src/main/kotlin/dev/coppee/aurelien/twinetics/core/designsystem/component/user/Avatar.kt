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

package dev.coppee.aurelien.twinetics.core.designsystem.component.user

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import dev.coppee.aurelien.twinetics.core.user.Rn3User
import dev.coppee.aurelien.twinetics.core.user.Rn3User.SignedInUser

@Composable
fun Rn3User.Avatar() {
    when (this@Avatar) {
        is SignedInUser -> SubcomposeAsyncImage(
            model = this@Avatar.pfpUri,
            contentDescription = null,
            loading = { FallbackPfp() },
            error = { FallbackPfp() },
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
        )

        else -> Icon(imageVector = Icons.Outlined.NoAccounts, contentDescription = null)
    }
}

@Composable
private fun FallbackPfp() {
    Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = null)
}
