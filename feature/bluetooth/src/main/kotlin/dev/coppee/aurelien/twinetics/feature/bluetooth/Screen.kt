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

package dev.coppee.aurelien.twinetics.feature.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.companion.AssociationInfo
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.IntentSender
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.coppee.aurelien.twinetics.core.designsystem.BottomBarItem
import dev.coppee.aurelien.twinetics.core.designsystem.R.color
import dev.coppee.aurelien.twinetics.core.designsystem.TopAppBarAction
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3Scaffold
import dev.coppee.aurelien.twinetics.core.designsystem.component.TopAppBarStyle.HOME
import dev.coppee.aurelien.twinetics.core.designsystem.component.getHaptic
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValues
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.padding
import dev.coppee.aurelien.twinetics.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.coppee.aurelien.twinetics.core.feedback.navigateToFeedback
import dev.coppee.aurelien.twinetics.core.model.data.SensorData
import dev.coppee.aurelien.twinetics.core.ui.TrackScreenViewEvent
import dev.coppee.aurelien.twinetics.core.user.Rn3User.SignedInUser
import dev.coppee.aurelien.twinetics.feature.bluetooth.R.string
import dev.coppee.aurelien.twinetics.feature.bluetooth.model.BluetoothUiState.Loading
import dev.coppee.aurelien.twinetics.feature.bluetooth.model.BluetoothUiState.Success
import dev.coppee.aurelien.twinetics.feature.bluetooth.model.BluetoothViewModel
import dev.coppee.aurelien.twinetics.feature.bluetooth.model.data.BluetoothData
import dev.coppee.aurelien.twinetics.feature.bluetooth.ui.Rn3BluetoothTileClick
import java.util.concurrent.Executor

@Composable
internal fun BluetoothRoute(
    modifier: Modifier = Modifier,
    viewModel: BluetoothViewModel = hiltViewModel(),
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToRecord: () -> Unit,
    navigateToHistory: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        Loading -> {}
        is Success -> BluetoothScreen(
            modifier = modifier,
            data = (uiState as Success).bluetoothData,
            feedbackTopAppBarAction = FeedbackScreenContext(
                localName = "BluetoothScreen",
                localID = "oujWHHHpuFbChUEYhyGX39V2exJ299Dw",
            ).toTopAppBarAction(navController::navigateToFeedback),
            onSettingsTopAppBarActionClicked = navigateToSettings,
            onHistoryBottomBarItemClicked = navigateToHistory,
            onRecordBottomBarItemClicked = navigateToRecord,
            onAddSensor = viewModel::addSensor,
        )
    }

    TrackScreenViewEvent(screenName = "bluetooth")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun BluetoothScreen(
    modifier: Modifier = Modifier,
    data: BluetoothData,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onSettingsTopAppBarActionClicked: () -> Unit = {},
    onHistoryBottomBarItemClicked: () -> Unit = {},
    onRecordBottomBarItemClicked: () -> Unit = {},
    onAddSensor: (sensorData: SensorData) -> Unit = {},
) {
    val haptic = getHaptic()
    val context = LocalContext.current

    val fabExpanded = remember { mutableStateOf(value = true) }

    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollPosition ->
                fabExpanded.value = scrollPosition == 0
            }
    }

    val deviceManager = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.getSystemService(Context.COMPANION_DEVICE_SERVICE) as CompanionDeviceManager
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    val executor = remember {
        Executor { runnable -> runnable.run() }
    }

    val selectDeviceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val bluetoothDevice = activityResult.data
                ?.getParcelableExtra<BluetoothDevice>(CompanionDeviceManager.EXTRA_DEVICE)
            bluetoothDevice?.createBond()
        }
    }

    fun startAssociation() {
        val deviceFilter = BluetoothDeviceFilter.Builder().build()

        val pairingRequest = AssociationRequest.Builder()
            .addDeviceFilter(deviceFilter)
            .setSingleDevice(false)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            deviceManager.associate(
                pairingRequest,
                executor,
                object : CompanionDeviceManager.Callback() {
                    override fun onAssociationPending(intentSender: IntentSender) {
                        selectDeviceLauncher.launch(
                            IntentSenderRequest.Builder(intentSender).build()
                        )
                    }

                    override fun onAssociationCreated(associationInfo: AssociationInfo) {
                        val associationId = associationInfo.id
                        val macAddress = associationInfo.deviceMacAddress
                        val displayName = associationInfo.displayName

                        onAddSensor(
                            SensorData(
                                address = macAddress.toString(),
                                name = displayName.toString(),
                                type = ""
                            )
                        )
                    }

                    override fun onFailure(errorMessage: CharSequence?) {
                    }
                }
            )
        }
    }

    val add = when (data.user) {
        is SignedInUser -> BottomBarItem(
            icon = Filled.RadioButtonChecked,
            label = stringResource(string.feature_bluetooth_bottomBar_record),
            onClick = onRecordBottomBarItemClicked,
            unselectedIconColor = Color(
                ContextCompat.getColor(
                    context,
                    color.core_designsystem_color,
                ),
            ),
            fullSize = true,
        )

        else -> BottomBarItem(
            icon = Filled.RadioButtonChecked,
            label = stringResource(string.feature_bluetooth_bottomBar_record),
            onClick = {
                haptic.click()

                Toast
                    .makeText(
                        context,
                        context.getString(string.feature_bluetooth_bottomBar_record_disabled),
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
        topAppBarTitle = stringResource(string.feature_bluetooth_topAppBarTitle),
        topAppBarTitleAlignment = CenterHorizontally,
        onBackIconButtonClicked = null,
        topAppBarActions = listOfNotNull(
            TopAppBarAction(
                icon = Outlined.Settings,
                title = stringResource(string.feature_bluetooth_topAppBarActions_settings),
                onClick = onSettingsTopAppBarActionClicked,
            ),
            feedbackTopAppBarAction,
        ),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = it,
                text = { Text(text = stringResource(string.feature_bluetooth_fab_text)) },
                icon = { Icon(imageVector = Outlined.Add, contentDescription = null) },
                onClick = {
                    startAssociation()

                    haptic.click()
                },
                expanded = fabExpanded.value,
            )
        },
        bottomBarItems = listOf(
            BottomBarItem(
                icon = Filled.Bluetooth,
                label = stringResource(string.feature_bluetooth_bottomBar_bluetooth),
                onClick = {},
                selected = true,
            ),
            add,
            BottomBarItem(
                icon = Filled.InsertChartOutlined,
                label = stringResource(string.feature_bluetooth_bottomBar_history),
                onClick = onHistoryBottomBarItemClicked,
            ),
        ),
        topAppBarStyle = HOME,
    ) {
        ColumnPanel(
            paddingValues = it,
            data = data,
            scrollState = scrollState,
        )
    }
}

@Composable
private fun ColumnPanel(
    paddingValues: Rn3PaddingValues,
    data: BluetoothData,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(paddingValues.add(bottom = 80.dp)),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        data.sensors.forEach { sensor ->
            Rn3BluetoothTileClick(
                sensor = sensor,
            )
        }
    }
}
