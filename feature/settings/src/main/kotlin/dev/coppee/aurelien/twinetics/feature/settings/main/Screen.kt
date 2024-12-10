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

package dev.coppee.aurelien.twinetics.feature.settings.main

import android.content.Context
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dev.coppee.aurelien.twinetics.core.analytics.LocalAnalyticsHelper
import dev.coppee.aurelien.twinetics.core.auth.LocalAuthHelper
import dev.coppee.aurelien.twinetics.core.common.openOssLicensesActivity
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewScreen
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewUiStates
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3Theme
import dev.coppee.aurelien.twinetics.core.designsystem.TopAppBarAction
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3ExpandableSurface
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3LargeButton
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3Scaffold
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3SurfaceDefaults
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3TextDefaults
import dev.coppee.aurelien.twinetics.core.designsystem.component.getHaptic
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileClick
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileClickConfirmationDialog
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.coppee.aurelien.twinetics.core.designsystem.component.user.UserAvatarAndName
import dev.coppee.aurelien.twinetics.core.designsystem.icons.Contract
import dev.coppee.aurelien.twinetics.core.designsystem.icons.DevicesOff
import dev.coppee.aurelien.twinetics.core.designsystem.icons.SyncSavedLocally
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValues
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValuesDirection.END
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.padding
import dev.coppee.aurelien.twinetics.core.designsystem.rn3ExpandVerticallyTransition
import dev.coppee.aurelien.twinetics.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.coppee.aurelien.twinetics.core.feedback.navigateToFeedback
import dev.coppee.aurelien.twinetics.core.ui.TrackScreenViewEvent
import dev.coppee.aurelien.twinetics.core.user.Rn3User.SignedInUser
import dev.coppee.aurelien.twinetics.feature.settings.R.string
import dev.coppee.aurelien.twinetics.feature.settings.accessibility.navigateToAccessibilitySettings
import dev.coppee.aurelien.twinetics.feature.settings.dataAndPrivacy.navigateToDataAndPrivacySettings
import dev.coppee.aurelien.twinetics.feature.settings.developer.main.navigateToDeveloperSettingsMain
import dev.coppee.aurelien.twinetics.feature.settings.logSettingsUiEvent
import dev.coppee.aurelien.twinetics.feature.settings.main.model.SettingsUiState
import dev.coppee.aurelien.twinetics.feature.settings.main.model.SettingsUiState.Loading
import dev.coppee.aurelien.twinetics.feature.settings.main.model.SettingsUiState.Success
import dev.coppee.aurelien.twinetics.feature.settings.main.model.SettingsViewModel
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.InAppUpdateState.DownloadingUpdate
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.InAppUpdateState.NoUpdateAvailable
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.InAppUpdateState.UpdateAvailable
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.InAppUpdateState.WaitingForRestart
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.PreviewParameterData
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.SettingsData
import dev.coppee.aurelien.twinetics.feature.settings.main.model.data.SettingsDataPreviewParameterProvider
import kotlinx.coroutines.launch

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    navigateToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val analytics = LocalAnalyticsHelper.current
    val auth = LocalAuthHelper.current

    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {}

    viewModel.setDevSettingsEnabled(context.areAndroidDeveloperSettingsOn())

    LaunchedEffect(Unit) {
        viewModel.checkForInAppUpdate(AppUpdateManagerFactory.create(context))
    }

    @Suppress("SpellCheckingInspection")
    SettingsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackIconButtonClicked = navController::popBackStack,
        feedbackTopAppBarAction = FeedbackScreenContext(
            localName = "SettingsScreen",
            localID = "niFsraaAjn2ceEtyaou8hBuxVcKZmL4d",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onAccountTileSwitchAccountTileClicked = {
            analytics.logSettingsUiEvent("accountTileSwitchAccountTile")
            navigateToLogin()
        },
        onAccountTileLogoutTileClicked = {
            analytics.logSettingsUiEvent("accountTileLogoutTile")

            viewModel.logout()
            scope.launch { auth.signOut(context) }

            navigateToLogin()
        },
        onAccountTileLoginButtonClicked = {
            analytics.logSettingsUiEvent("accountTileLoginButton")
            navigateToLogin()
        },
        onUpdateAvailableTileClicked = {
            viewModel.performInAppUpdateAction(context, launcher)
        },
        onClickDataAndPrivacyTile = {
            analytics.logSettingsUiEvent("dataAndPrivacyTile")
            navController.navigateToDataAndPrivacySettings()
        },
        onClickAccessibilityTile = {
            analytics.logSettingsUiEvent("accessibilityTile")
            navController.navigateToAccessibilitySettings()
        },
        onClickDeveloperSettingsTile = {
            analytics.logSettingsUiEvent("developerSettingsTile")
            navController.navigateToDeveloperSettingsMain()
        },
        onClickOssLicensesTile = {
            analytics.logSettingsUiEvent("ossLicensesTile")
            context.openOssLicensesActivity()
        },
    )

    TrackScreenViewEvent(screenName = "Settings")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onBackIconButtonClicked: () -> Unit = {},
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onAccountTileSwitchAccountTileClicked: () -> Unit = {},
    onAccountTileLogoutTileClicked: () -> Unit = {},
    onAccountTileLoginButtonClicked: () -> Unit = {},
    onUpdateAvailableTileClicked: () -> Unit = {},
    onClickDataAndPrivacyTile: () -> Unit = {},
    onClickAccessibilityTile: () -> Unit = {},
    onClickDeveloperSettingsTile: () -> Unit = {},
    onClickOssLicensesTile: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_settings_settingsScreen_topAppBarTitle),
        onBackIconButtonClicked = onBackIconButtonClicked,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> SettingsPanel(
                paddingValues = it,
                data = uiState.settingsData,
                onAccountTileSwitchAccountTileClicked = onAccountTileSwitchAccountTileClicked,
                onAccountTileLogoutTileClicked = onAccountTileLogoutTileClicked,
                onAccountTileLoginButtonClicked = onAccountTileLoginButtonClicked,
                onUpdateAvailableTileClicked = onUpdateAvailableTileClicked,
                onClickDataAndPrivacyTile = onClickDataAndPrivacyTile,
                onClickAccessibilityTile = onClickAccessibilityTile,
                onClickDeveloperSettingsTile = onClickDeveloperSettingsTile,
                onClickOssLicensesTile = onClickOssLicensesTile,
            )
        }
    }
}

@Composable
private fun SettingsPanel(
    paddingValues: Rn3PaddingValues,
    data: SettingsData,
    onAccountTileSwitchAccountTileClicked: () -> Unit,
    onAccountTileLogoutTileClicked: () -> Unit,
    onAccountTileLoginButtonClicked: () -> Unit,
    onUpdateAvailableTileClicked: () -> Unit,
    onClickDataAndPrivacyTile: () -> Unit,
    onClickAccessibilityTile: () -> Unit,
    onClickDeveloperSettingsTile: () -> Unit,
    onClickOssLicensesTile: () -> Unit,
) {
    val haptic = getHaptic()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
    ) {
        // accountTile
        when (data.user) {
            is SignedInUser -> Rn3ExpandableSurface(
                content = { data.user.UserAvatarAndName(showEmail = it) },
                expandedContent = {
                    Column {
                        Rn3TileHorizontalDivider(
                            paddingValues = Rn3SurfaceDefaults.paddingValues.copy(
                                top = 0.dp,
                                bottom = 0.dp,
                            ),
                        )

                        // switchAccountTile
                        Rn3TileClick(
                            title = stringResource(string.feature_settings_settingsScreen_switchAccountTile_title),
                            icon = Outlined.Group,
                            onClick = onAccountTileSwitchAccountTileClicked,
                        )

                        // logoutTile
                        Rn3TileClickConfirmationDialog(
                            title = stringResource(string.feature_settings_settingsScreen_accountLogoutTile_title),
                            icon = Icons.AutoMirrored.Outlined.Logout,
                            bodyHeader = stringResource(string.feature_settings_settingsScreen_accountLogoutTile_bodyHeader),
                            bodyBulletPoints = mapOf(
                                Outlined.SyncSavedLocally to stringResource(string.feature_settings_settingsScreen_accountLogoutTile_body_currentDataStoredLocallyAndContinueToWorkOnDevice),
                                Outlined.DevicesOff to stringResource(string.feature_settings_settingsScreen_accountLogoutTile_body_countersNotAvailableOnOtherDevices),
                                Outlined.Warning to stringResource(string.feature_settings_settingsScreen_accountLogoutTile_body_mayLoseDataIfSomethingHappensToDevice),
                            ),
                            onClick = {
                                onAccountTileLogoutTileClicked()
                            },
                        )
                    }
                },
            )

            else -> Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Rn3SurfaceDefaults.paddingValues),
                shape = Rn3SurfaceDefaults.shape,
                tonalElevation = Rn3SurfaceDefaults.tonalElevation,
            ) {
                Row(
                    modifier = Modifier.padding(Rn3TextDefaults.paddingValues),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    data.user.UserAvatarAndName()

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minWidth = 16.dp),
                    )

                    // loginButton
                    OutlinedButton(onClick = onAccountTileLoginButtonClicked) {
                        Text(text = stringResource(string.feature_settings_settingsScreen_accountLoginTile_loginButton_text))
                    }
                }
            }
        }

        // updateAvailableTile
        with(data.inAppUpdateData) {
            AnimatedVisibility(
                visible = shouldShowTile(),
                enter = rn3ExpandVerticallyTransition(),
            ) {
                val color by animateColorAsState(
                    targetValue = if (actionPossible()) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                            Rn3SurfaceDefaults.tonalElevation,
                        )
                    },
                    label = "Rn3UpdateTileDefaults background color",
                )

                Rn3LargeButton(
                    modifier = Modifier.padding(Rn3SurfaceDefaults.paddingValues.copy(top = 0.dp)),
                    text = {
                        Text(
                            text = when (this@with) {
                                is UpdateAvailable -> stringResource(string.feature_settings_settingsScreen_updateAvailable)
                                is DownloadingUpdate -> stringResource(string.feature_settings_settingsScreen_updateDownloading)
                                is WaitingForRestart -> stringResource(string.feature_settings_settingsScreen_updateRestart)
                                NoUpdateAvailable -> "RahNeil_N3:GsKMAaulylNilvukEZCbJI4jWLXbRRzb"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.contentColorFor(color),
                            modifier = Modifier.padding(Rn3TextDefaults.paddingValues.only(END)),
                        )
                    },
                    icon = {
                        when (this@with) {
                            is DownloadingUpdate -> {
                                val progressFloat by animateFloatAsState(
                                    targetValue = progress,
                                    label = "inAppUpdate CircularProgressIndicator progress",
                                )
                                CircularProgressIndicator(
                                    progress = { progressFloat },
                                    modifier = Modifier.size(24.dp),
                                    trackColor = MaterialTheme.colorScheme.surface,
                                )
                            }

                            NoUpdateAvailable -> {}
                            is UpdateAvailable -> Icon(
                                imageVector = Outlined.FileDownload,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )

                            is WaitingForRestart -> Icon(
                                imageVector = Outlined.RestartAlt,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    },
                    color = color,
                ) {
                    if (actionPossible()) {
                        haptic.click()
                        onUpdateAvailableTileClicked()
                    }
                }
            }
        }

        // generalHeaderTile
        Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_generalHeaderTile_title))

        // dataAndPrivacyTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_dataAndPrivacyTile_title),
            icon = Outlined.Shield,
            onClick = onClickDataAndPrivacyTile,
        )

        // accessibilityTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_accessibilityTile_title),
            icon = Outlined.AccessibilityNew,
            onClick = onClickAccessibilityTile,
        )

        Rn3TileHorizontalDivider()

        // otherHeaderTile
        Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_otherHeaderTile_title))

        // developerSettingsTile
        if (data.devSettingsEnabled) {
            Rn3TileClick(
                title = stringResource(string.feature_settings_settingsScreen_developer_title),
                icon = Outlined.DataObject,
                supportingText = stringResource(string.feature_settings_settingsScreen_developer_supportingText),
                onClick = onClickDeveloperSettingsTile,
            )
        }

        // ossLicensesTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_ossLicensesTile_title),
            icon = Outlined.Contract,
            onClick = onClickOssLicensesTile,
        )
    }
}

@Composable
private fun Context.areAndroidDeveloperSettingsOn(): Boolean {
    if (LocalInspectionMode.current) return true

    return Settings.Secure.getInt(
        this.contentResolver,
        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
        0,
    ) != 0
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        SettingsScreen(
            uiState = Success(PreviewParameterData.settingsData_default),
        )
    }
}

@Rn3PreviewUiStates
@Composable
private fun UiStates(
    @PreviewParameter(SettingsDataPreviewParameterProvider::class)
    settingsData: SettingsData,
) {
    Rn3Theme {
        SettingsScreen(
            uiState = Success(settingsData = settingsData),
        )
    }
}
