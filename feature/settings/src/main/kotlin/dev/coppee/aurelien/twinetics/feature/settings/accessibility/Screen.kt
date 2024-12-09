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

package dev.coppee.aurelien.twinetics.feature.settings.accessibility

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.SettingsAccessibility
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.coppee.aurelien.twinetics.core.accessibility.AccessibilityHelper
import dev.coppee.aurelien.twinetics.core.accessibility.LocalAccessibilityHelper
import dev.coppee.aurelien.twinetics.core.analytics.LocalAnalyticsHelper
import dev.coppee.aurelien.twinetics.core.common.openAndroidAccessibilitySettingsActivity
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewScreen
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewUiStates
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3Theme
import dev.coppee.aurelien.twinetics.core.designsystem.TopAppBarAction
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3Scaffold
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileClick
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileSwitch
import dev.coppee.aurelien.twinetics.core.designsystem.icons.Tooltip
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValues
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.padding
import dev.coppee.aurelien.twinetics.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.coppee.aurelien.twinetics.core.feedback.navigateToFeedback
import dev.coppee.aurelien.twinetics.core.ui.TrackScreenViewEvent
import dev.coppee.aurelien.twinetics.feature.settings.R.string
import dev.coppee.aurelien.twinetics.feature.settings.accessibility.model.AccessibilitySettingsUiState
import dev.coppee.aurelien.twinetics.feature.settings.accessibility.model.AccessibilitySettingsUiState.Loading
import dev.coppee.aurelien.twinetics.feature.settings.accessibility.model.AccessibilitySettingsUiState.Success
import dev.coppee.aurelien.twinetics.feature.settings.accessibility.model.AccessibilitySettingsViewModel
import dev.coppee.aurelien.twinetics.feature.settings.accessibility.model.data.AccessibilitySettingsData
import dev.coppee.aurelien.twinetics.feature.settings.accessibility.model.data.AccessibilitySettingsDataPreviewParameterProvider
import dev.coppee.aurelien.twinetics.feature.settings.accessibility.model.data.PreviewParameterData
import dev.coppee.aurelien.twinetics.feature.settings.logDataAndPrivacySettingsUiEvent

@Composable
internal fun AccessibilitySettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: AccessibilitySettingsViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val analyticsHelper = LocalAnalyticsHelper.current

    AccessibilitySettingsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackIconButtonClicked = navController::popBackStack,
        feedbackTopAppBarAction = FeedbackScreenContext(
            localName = "AccessibilitySettingsScreen",
            localID = "jrKt4Xe58KDipPJsm1iPUijn6BMsNc8g",
        ).toTopAppBarAction(navController::navigateToFeedback),
        setEmphasizedSwitches = viewModel::setEmphasizedSwitches,
        setIconTooltips = viewModel::setIconTooltips,
        setAltText = viewModel::setAltText,
        onClickAndroidAccessibilityTile = {
            analyticsHelper.logDataAndPrivacySettingsUiEvent("androidAccessibilityTile")
            context.openAndroidAccessibilitySettingsActivity()
        },
    )

    TrackScreenViewEvent(screenName = "AccessibilitySettings")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun AccessibilitySettingsScreen(
    modifier: Modifier = Modifier,
    uiState: AccessibilitySettingsUiState,
    onBackIconButtonClicked: () -> Unit = {},
    feedbackTopAppBarAction: TopAppBarAction? = null,
    setEmphasizedSwitches: (Boolean) -> Unit = {},
    setIconTooltips: (Boolean) -> Unit = {},
    setAltText: (Boolean) -> Unit = {},
    onClickAndroidAccessibilityTile: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_settings_accessibilitySettingsScreen_topAppBarTitle),
        onBackIconButtonClicked = onBackIconButtonClicked,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> AccessibilitySettingsPanel(
                paddingValues = it,
                data = uiState.accessibilitySettingsData,
                setEmphasizedSwitches = setEmphasizedSwitches,
                setIconTooltips = setIconTooltips,
                setAltText = setAltText,
                onClickAndroidAccessibilityTile = onClickAndroidAccessibilityTile,
            )
        }
    }
}

@Composable
private fun AccessibilitySettingsPanel(
    paddingValues: Rn3PaddingValues,
    data: AccessibilitySettingsData,
    setEmphasizedSwitches: (Boolean) -> Unit,
    setIconTooltips: (Boolean) -> Unit,
    setAltText: (Boolean) -> Unit,
    onClickAndroidAccessibilityTile: () -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
    ) {
        // emphasizedSwitchesTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_settingsScreen_emphasizedSwitchesTile_title),
            icon = Outlined.ToggleOn,
            checked = data.hasEmphasizedSwitchesEnabled,
            onCheckedChange = setEmphasizedSwitches,
        )

        // iconTooltipsTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_settingsScreen_iconTooltipsTile_title),
            icon = Outlined.Tooltip,
            supportingText = stringResource(string.feature_settings_settingsScreen_iconTooltipsTile_supportingText),
            checked = data.hasIconTooltipsEnabled,
            onCheckedChange = setIconTooltips,
        )

        // iconTooltipsTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_settingsScreen_iconAltTextTile_title),
            icon = Outlined.Description,
            supportingText = stringResource(string.feature_settings_settingsScreen_iconAltTextTile_supportingText),
            checked = data.hasAltTextEnabled,
            onCheckedChange = setAltText,
        )

        Rn3TileHorizontalDivider()

        // androidAccessibilityTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_androidAccessibilityTile_title),
            icon = Outlined.SettingsAccessibility,
            onClick = onClickAndroidAccessibilityTile,
            external = true,
        )
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        AccessibilitySettingsScreen(
            uiState = Success(PreviewParameterData.accessibilitySettingsData_default),
        )
    }
}

@Rn3PreviewUiStates
@Composable
private fun UiStates(
    @PreviewParameter(AccessibilitySettingsDataPreviewParameterProvider::class)
    accessibilitySettingsData: AccessibilitySettingsData,
) {
    CompositionLocalProvider(
        value = LocalAccessibilityHelper provides AccessibilityHelper(
            hasEmphasizedSwitchesEnabled = accessibilitySettingsData.hasEmphasizedSwitchesEnabled,
            hasIconTooltipsEnabled = accessibilitySettingsData.hasIconTooltipsEnabled,
            hasAltTextEnabled = accessibilitySettingsData.hasAltTextEnabled,
        ),
    ) {
        Rn3Theme {
            AccessibilitySettingsScreen(uiState = Success(accessibilitySettingsData = accessibilitySettingsData))
        }
    }
}
