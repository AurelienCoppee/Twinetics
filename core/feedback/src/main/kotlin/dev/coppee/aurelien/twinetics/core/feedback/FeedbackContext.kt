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

package dev.coppee.aurelien.twinetics.core.feedback

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.coppee.aurelien.twinetics.core.designsystem.TopAppBarAction
import dev.coppee.aurelien.twinetics.core.feedback.R.string

sealed interface FeedbackContext {
    data object FeedbackEmptyContext : FeedbackContext
    data class FeedbackScreenContext(
        private val localName: String,
        private val localID: String,
    ) : FeedbackContext {

        fun getID(context: Context): String {
            return "RahNeil_N3:$localID:$localName:" + context.resources.getString(string.core_feedback_localeID)
        }

        @Composable
        fun toTopAppBarAction(navigateToFeedback: (Context, FeedbackContext) -> Unit): TopAppBarAction =
            LocalContext.current.let { context ->
                TopAppBarAction(
                    icon = Icons.Outlined.Feedback,
                    title = stringResource(string.core_feedback_topAppBarAction_title),
                ) {
                    navigateToFeedback(context, this)
                }
            }
    }
}
