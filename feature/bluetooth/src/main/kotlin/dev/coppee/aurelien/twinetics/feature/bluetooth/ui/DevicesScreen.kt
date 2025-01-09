package dev.coppee.aurelien.twinetics.feature.bluetooth.ui

import android.companion.CompanionDeviceManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dev.coppee.aurelien.twinetics.core.bluetooth.companion.AssociatedDeviceCompat
import dev.coppee.aurelien.twinetics.core.bluetooth.companion.getAssociatedDevices
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DevicesScreen(
    deviceManager: CompanionDeviceManager,
    onConnect: (AssociatedDeviceCompat) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var associatedDevices by remember {
        mutableStateOf(deviceManager.getAssociatedDevices())
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        LaunchedEffect(associatedDevices) {
            associatedDevices.forEach {
                deviceManager.startObservingDevicePresence(it.address)
            }
        }
    }

    AssociatedDevicesList(
        associatedDevices = associatedDevices,
        onConnect = onConnect,
        onDisassociate = {
            scope.launch {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    deviceManager.disassociate(it.id)
                } else {
                    @Suppress("DEPRECATION")
                    deviceManager.disassociate(it.address)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    deviceManager.stopObservingDevicePresence(it.address)
                }
                associatedDevices = deviceManager.getAssociatedDevices()
            }
        },
    )
}