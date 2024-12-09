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

package dev.coppee.aurelien.twinetics.feature.feed.friends

import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dev.coppee.aurelien.twinetics.core.designsystem.BottomBarItem
import dev.coppee.aurelien.twinetics.core.designsystem.R.color
import dev.coppee.aurelien.twinetics.core.designsystem.TopAppBarAction
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3Scaffold
import dev.coppee.aurelien.twinetics.core.designsystem.component.TopAppBarStyle.HOME
import dev.coppee.aurelien.twinetics.core.designsystem.component.getHaptic
import dev.coppee.aurelien.twinetics.core.designsystem.icons.HumanGreetingProximity
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValues
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.padding
import dev.coppee.aurelien.twinetics.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.coppee.aurelien.twinetics.core.feedback.navigateToFeedback
import dev.coppee.aurelien.twinetics.core.ui.TrackScreenViewEvent
import dev.coppee.aurelien.twinetics.core.user.Rn3User.AnonymousUser
import dev.coppee.aurelien.twinetics.core.user.Rn3User.SignedInUser
import dev.coppee.aurelien.twinetics.feature.feed.R.string
import dev.coppee.aurelien.twinetics.feature.feed.friends.model.FriendsFeedUiState.Loading
import dev.coppee.aurelien.twinetics.feature.feed.friends.model.FriendsFeedUiState.Success
import dev.coppee.aurelien.twinetics.feature.feed.friends.model.FriendsFeedViewModel
import dev.coppee.aurelien.twinetics.feature.feed.friends.model.data.FriendsFeedData
import dev.coppee.aurelien.twinetics.feature.feed.ui.Publication

@Composable
internal fun FriendsFeedRoute(
    modifier: Modifier = Modifier,
    viewModel: FriendsFeedViewModel = hiltViewModel(),
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToConnect: () -> Unit,
    navigateToPublication: () -> Unit,
    navigateToPublic: () -> Unit,
    navigateToEvents: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        Loading -> {}
        is Success -> FriendsFeedScreen(
            modifier = modifier,
            data = (uiState as Success).friendsFeedData,
            feedbackTopAppBarAction = FeedbackScreenContext(
                localName = "FriendsFeedScreen",
                localID = "GSChPsxOkpZYCqOdHXt9iZkNwdm0oJNj",
            ).toTopAppBarAction(navController::navigateToFeedback),
            onSettingsTopAppBarActionClicked = navigateToSettings,
            onConnectBottomBarItemClicked = navigateToConnect,
            onAddBottomBarItemClicked = navigateToPublication,
            onPublicBottomBarItemClicked = navigateToPublic,
            onEventsBottomBarItemClicked = navigateToEvents,
        )
    }

    TrackScreenViewEvent(screenName = "FriendsFeed")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun FriendsFeedScreen(
    modifier: Modifier = Modifier,
    data: FriendsFeedData,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onSettingsTopAppBarActionClicked: () -> Unit = {},
    onConnectBottomBarItemClicked: () -> Unit = {},
    onAddBottomBarItemClicked: () -> Unit = {},
    onPublicBottomBarItemClicked: () -> Unit = {},
    onEventsBottomBarItemClicked: () -> Unit = {},
) {
    val haptic = getHaptic()
    val context = LocalContext.current

    val add = when (data.user) {
        is SignedInUser -> BottomBarItem(
            icon = Filled.Add,
            label = stringResource(string.feature_feed_bottomBar_add),
            onClick = onAddBottomBarItemClicked,
            unselectedIconColor = Color(
                ContextCompat.getColor(
                    context,
                    color.core_designsystem_color,
                ),
            ),
            fullSize = true,
        )

        else -> BottomBarItem(
            icon = Filled.Add,
            label = stringResource(string.feature_feed_bottomBar_add),
            onClick = {
                haptic.click()

                Toast
                    .makeText(
                        context,
                        context.getString(string.feature_feed_bottomBar_add_disabled),
                        Toast.LENGTH_SHORT,
                    )
                    .show()
            },
            unselectedIconColor = MaterialTheme.colorScheme.secondaryContainer,
            fullSize = true,
        )
    }

    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_feed_topAppBarTitle_friends),
        topAppBarTitleAlignment = CenterHorizontally,
        onBackIconButtonClicked = null,
        topAppBarActions = listOfNotNull(
            TopAppBarAction(
                icon = Outlined.Settings,
                title = stringResource(string.feature_feed_topAppBarActions_settings),
                onClick = onSettingsTopAppBarActionClicked,
            ),
            feedbackTopAppBarAction,
        ),
        bottomBarItems = listOf(
            BottomBarItem(
                icon = Outlined.HumanGreetingProximity,
                label = stringResource(string.feature_feed_bottomBar_connect),
                onClick = onConnectBottomBarItemClicked,
            ),
            BottomBarItem(
                icon = Filled.People,
                label = stringResource(string.feature_feed_bottomBar_friends),
                onClick = { },
                selected = true,
            ),
            add,
            BottomBarItem(
                icon = Filled.Public,
                label = stringResource(string.feature_feed_bottomBar_public),
                onClick = onPublicBottomBarItemClicked,
            ),
            BottomBarItem(
                icon = Filled.Event,
                label = stringResource(string.feature_feed_bottomBar_events),
                onClick = onEventsBottomBarItemClicked,
            ),
        ),
        topAppBarStyle = HOME,
    ) {
        FriendsFeedPanel(
            paddingValues = it,
            data = data,
        )
    }
}

@Composable
private fun FriendsFeedPanel(
    paddingValues: Rn3PaddingValues,
    data: FriendsFeedData,
) {
    val phoneUtil = PhoneNumberUtil.getInstance()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
    ) {
        data.posts.forEach { post ->
            Publication(
                post = post,
                friend = data.friends.find { it.userId == post.userId },
                enabled = if (data.user is SignedInUser || data.user is AnonymousUser) {
                    phoneUtil.isValidNumber(data.phone)
                } else {
                    false
                },
            )
        }
    }
}
