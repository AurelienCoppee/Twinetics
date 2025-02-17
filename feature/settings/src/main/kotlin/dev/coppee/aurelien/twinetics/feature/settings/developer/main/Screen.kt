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

package dev.coppee.aurelien.twinetics.feature.settings.developer.main

import android.text.format.DateUtils
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.DataArray
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.FirebaseApp
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.LAST_FETCH_STATUS_FAILURE
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.LAST_FETCH_STATUS_SUCCESS
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.LAST_FETCH_STATUS_THROTTLED
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.VALUE_SOURCE_REMOTE
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.getInstance
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewScreen
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewUiStates
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3Theme
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3Scaffold
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileClick
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileClickConfirmationDialog
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileCopy
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileSwitch
import dev.coppee.aurelien.twinetics.core.designsystem.icons.Logo
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValues
import dev.coppee.aurelien.twinetics.core.ui.TrackScreenViewEvent
import dev.coppee.aurelien.twinetics.feature.settings.BuildConfig
import dev.coppee.aurelien.twinetics.feature.settings.R.string
import dev.coppee.aurelien.twinetics.feature.settings.developer.links.navigateToDeveloperSettingsLinks
import dev.coppee.aurelien.twinetics.feature.settings.developer.main.model.DeveloperSettingsUiState
import dev.coppee.aurelien.twinetics.feature.settings.developer.main.model.DeveloperSettingsViewModel
import dev.coppee.aurelien.twinetics.feature.settings.developer.main.model.data.DeveloperSettingsData
import dev.coppee.aurelien.twinetics.feature.settings.developer.main.model.data.DeveloperSettingsDataPreviewParameterProvider
import dev.coppee.aurelien.twinetics.feature.settings.developer.main.model.data.PreviewParameterData
import java.util.UUID

@Composable
internal fun DeveloperSettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: DeveloperSettingsViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeveloperSettingsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackIconButtonClicked = navController::popBackStack,
        onLinksRn3UrlTileClicked = navController::navigateToDeveloperSettingsLinks,
        onClearPersistenceTileClicked = viewModel::clearPersistence,
        onSimulateCrashTileClicked = {
            throw RuntimeException("RahNeil_N3:SimulateCrashTile:FakeCrash (${UUID.randomUUID()})")
        },
    )

    TrackScreenViewEvent(screenName = "DeveloperSettings")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DeveloperSettingsScreen(
    modifier: Modifier = Modifier,
    uiState: DeveloperSettingsUiState,
    onBackIconButtonClicked: () -> Unit = {},
    onLinksRn3UrlTileClicked: () -> Unit = {},
    onClearPersistenceTileClicked: () -> Unit = {},
    onSimulateCrashTileClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_settings_developerSettingsScreen_topAppBarTitle),
        onBackIconButtonClicked = onBackIconButtonClicked,
    ) {
        DeveloperSettingsPanel(
            paddingValues = it,
            data = uiState.developerSettingsData,
            onLinksRn3UrlTileClicked = onLinksRn3UrlTileClicked,
            onClearPersistenceTileClicked = onClearPersistenceTileClicked,
            onSimulateCrashTileClicked = onSimulateCrashTileClicked,
        )
    }
}

@Composable
private fun DeveloperSettingsPanel(
    paddingValues: Rn3PaddingValues,
    data: DeveloperSettingsData,
    onLinksRn3UrlTileClicked: () -> Unit,
    onClearPersistenceTileClicked: () -> Unit,
    onSimulateCrashTileClicked: () -> Unit,
) {
    val context = LocalContext.current

    LazyColumn(contentPadding = paddingValues.toComposePaddingValues()) {
        // buildconfigTile
        item {
            Rn3TileClick(
                title = stringResource(string.feature_settings_developerSettingsScreen_buildconfigTile_title),
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Logo,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                supportingContent = { Text(text = "${BuildConfig.FLAVOR} / ${BuildConfig.BUILD_TYPE}") },
            ) {}
        }

        // linksRn3UrlTile
        item {
            data.isUserAdmin.let { enabled ->
                Rn3TileClick(
                    title = stringResource(string.feature_settings_developerSettingsScreen_linksRn3UrlTile_title),
                    icon = Icons.Outlined.Link,
                    onClick = onLinksRn3UrlTileClicked,
                    enabled = enabled,
                    supportingText = stringResource(string.feature_settings_developerSettingsScreen_linksRn3UrlTile_supportingText).takeUnless { enabled },
                )
            }
        }

        // clearPersistenceTile
        item {
            @Suppress("KotlinConstantConditions")
            (BuildConfig.FLAVOR == "demo").let { enabled ->
                Rn3TileClickConfirmationDialog(
                    title = stringResource(string.feature_settings_developerSettingsScreen_clearPersistenceTile_title),
                    icon = Icons.Outlined.DeleteForever,
                    body = {},
                    supportingText = stringResource(string.feature_settings_developerSettingsScreen_clearPersistenceTile_supportingText).takeUnless { enabled },
                    enabled = enabled,
                    onClick = onClearPersistenceTileClicked,
                )
            }
        }

        // simulateCrashTile
        item {
            (BuildConfig.DEBUG || data.isUserAdmin).let { enabled ->
                Rn3TileClickConfirmationDialog(
                    title = stringResource(string.feature_settings_developerSettingsScreen_simulateCrashTile_title),
                    icon = Icons.Outlined.Report,
                    body = {},
                    supportingText = stringResource(string.feature_settings_developerSettingsScreen_simulateCrashTile_supportingText).takeUnless { enabled },
                    onClick = onSimulateCrashTileClicked,
                    enabled = enabled,
                )
            }
        }

        FirebaseApp.getApps(context)
            .map {
                Triple(
                    it.name,
                    getInstance(it),
                    FirebaseInstallations.getInstance(it).id,
                )
            }
            .forEach { (appName, remoteConfig, installation) ->
                item { Rn3TileHorizontalDivider() }

                item {
                    Rn3TileSmallHeader(
                        title = stringResource(
                            string.feature_settings_developerSettingsScreen_configHeaderTile_title,
                            appName,
                        ),
                    )
                }

                item {
                    var installationID: String? by rememberSaveable { mutableStateOf(null) }
                    installation.addOnSuccessListener { installationID = it }

                    Rn3TileCopy(
                        title = stringResource(string.feature_settings_developerSettingsScreen_firebaseIdTile_title),
                        icon = Icons.Outlined.LocalFireDepartment,
                        text = installationID
                            ?: stringResource(string.feature_settings_developerSettingsScreen_firebaseIdTile_text_loading),
                    )
                }

                item {
                    Rn3TileClick(
                        title = stringResource(string.feature_settings_developerSettingsScreen_rcStatusTile_title),
                        icon = Icons.Outlined.DataArray,
                        supportingText = when (remoteConfig.info.lastFetchStatus) {
                            LAST_FETCH_STATUS_SUCCESS ->
                                stringResource(
                                    string.feature_settings_developerSettingsScreen_rcStatusTile_success,
                                    DateUtils.getRelativeTimeSpanString(
                                        remoteConfig.info.fetchTimeMillis,
                                    ),
                                )

                            LAST_FETCH_STATUS_FAILURE -> stringResource(string.feature_settings_developerSettingsScreen_rcStatusTile_failure)
                            LAST_FETCH_STATUS_THROTTLED -> stringResource(string.feature_settings_developerSettingsScreen_rcStatusTile_throttled)
                            LAST_FETCH_STATUS_NO_FETCH_YET -> stringResource(string.feature_settings_developerSettingsScreen_rcStatusTile_noFetchYet)
                            else -> "RahNeil_N3:Error:NMdUsSOSmdgHuvcFuFr6WjorE25ZszWZ"
                        },
                    ) {}
                }

                remoteConfig.all.entries.forEach { (key, value) ->
                    val icon = when (value.source) {
                        VALUE_SOURCE_REMOTE -> Icons.Outlined.CloudDone
                        VALUE_SOURCE_DEFAULT -> Icons.AutoMirrored.Outlined.InsertDriveFile
                        else -> Icons.Outlined.QuestionMark
                    }

                    with(value.asString()) {
                        if (toBooleanStrictOrNull() is Boolean) {
                            item {
                                Rn3TileSwitch(
                                    title = key,
                                    icon = icon,
                                    checked = toBoolean(),
                                ) {}
                            }
                        } else {
                            item {
                                Rn3TileCopy(title = key, icon = icon, text = this@with)
                            }
                        }
                    }
                }
            }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        DeveloperSettingsScreen(
            uiState = DeveloperSettingsUiState(PreviewParameterData.developerSettingsData_default),
        )
    }
}

@Rn3PreviewUiStates
@Composable
private fun UiStates(
    @PreviewParameter(DeveloperSettingsDataPreviewParameterProvider::class)
    developerSettingsData: DeveloperSettingsData,
) {
    Rn3Theme {
        DeveloperSettingsScreen(
            uiState = DeveloperSettingsUiState(developerSettingsData),
        )
    }
}
