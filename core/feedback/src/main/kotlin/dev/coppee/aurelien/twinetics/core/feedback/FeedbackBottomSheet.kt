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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3IconButton
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3SurfaceDefaults
import dev.coppee.aurelien.twinetics.core.feedback.R.string
import dev.coppee.aurelien.twinetics.core.feedback.pages.FeedbackContextPage
import dev.coppee.aurelien.twinetics.core.feedback.pages.FeedbackDescriptionPage
import dev.coppee.aurelien.twinetics.core.feedback.pages.FeedbackTypePage
import dev.coppee.aurelien.twinetics.core.ui.TrackScreenViewEvent
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FeedbackBottomSheet(
    navController: NavController,
    contextID: String?,
    pageDef: String,
    typeDef: String,
    descriptionDef: String,
    onCurrentPageDef: Boolean,
    sendScreenshotDef: Boolean,
    sendAdditionalInfoDef: Boolean,
) {
    val sheetState =
        rememberModalBottomSheetState(confirmValueChange = { false }, skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            navController.popBackStack()
        },
        sheetState = sheetState,
        scrimColor = Color.Transparent,
        dragHandle = {},
    ) {
        Column {
            Surface(tonalElevation = Rn3SurfaceDefaults.tonalElevation) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp, start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        Icon(imageVector = Outlined.Feedback, contentDescription = null)

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = stringResource(string.core_feedback_topBar_title),
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minWidth = 16.dp),
                    )

                    Rn3IconButton(
                        icon = Outlined.Close,
                        contentDescription = stringResource(string.core_feedback_closeButton_contentDescription),
                    ) {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            navController.popBackStack()
                        }
                    }
                }
            }

            Box {
                var page by rememberSaveable { mutableStateOf(pageDef) }

                var type by rememberSaveable { mutableStateOf(typeDef) }
                var description by rememberSaveable { mutableStateOf(descriptionDef) }
                var onCurrentPage by rememberSaveable { mutableStateOf(onCurrentPageDef) }
                var sendScreenshot by rememberSaveable { mutableStateOf(sendScreenshotDef) }
                var sendAdditionalInfo by rememberSaveable { mutableStateOf(sendAdditionalInfoDef) }

                AnimatedContent(
                    targetState = page,
                    transitionSpec = {
                        (expandVertically { height -> height } + fadeIn()).togetherWith(
                            exit = shrinkVertically { height -> height } + fadeOut(),
                        )
                            .using(SizeTransform(clip = false))
                    },
                    label = "animatedPage",
                ) { screen ->
                    when (screen) {
                        "type" -> FeedbackTypePage(type) {
                            // Reset description if changed type of feedback
                            // if not changed we keep the description so it isn't lost
                            if (it != type) description = ""

                            type = it
                            page = "context"
                        }

                        "context" -> FeedbackContextPage(
                            bug = type == "BUG",
                            hasContext = contextID != null,
                            onCurrentPage = onCurrentPage,
                            sendScreenshot = sendScreenshot,
                            sendAdditionalInfo = sendAdditionalInfo,
                            nextPage = { onCurrentPageValue, sendScreenshotValue, sendAdditionalInfoValue ->
                                onCurrentPage = onCurrentPageValue
                                sendScreenshot = sendScreenshotValue
                                sendAdditionalInfo = sendAdditionalInfoValue

                                page = "description"
                            },
                            previousPage = { onCurrentPageValue, sendScreenshotValue, sendAdditionalInfoValue ->
                                onCurrentPage = onCurrentPageValue
                                sendScreenshot = sendScreenshotValue
                                sendAdditionalInfo = sendAdditionalInfoValue

                                page = "type"
                            },
                        )

                        "description" -> FeedbackDescriptionPage(
                            bug = type == "BUG",
                            feedbackDescription = description,
                            nextPage = {
                                description = it

                                TODO()
                            },
                            previousPage = {
                                description = it

                                page = "context"
                            },
                        )
                    }
                }
            }
        }
    }

    TrackScreenViewEvent(screenName = "FeedbackBottomSheet")
}
