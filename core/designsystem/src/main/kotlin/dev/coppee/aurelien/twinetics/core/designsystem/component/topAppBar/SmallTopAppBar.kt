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

package dev.coppee.aurelien.twinetics.core.designsystem.component.topAppBar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Outlined
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.coppee.aurelien.twinetics.core.designsystem.DropdownMenu
import dev.coppee.aurelien.twinetics.core.designsystem.R.string
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewComponentDefault
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3PreviewComponentVariation
import dev.coppee.aurelien.twinetics.core.designsystem.Rn3Theme
import dev.coppee.aurelien.twinetics.core.designsystem.TopAppBarAction
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3IconButton

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Rn3SmallTopAppBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackIconButtonClicked: (() -> Unit)? = null,
    actions: List<TopAppBarAction> = emptyList(),
    transparent: Boolean = false,
    title: @Composable () -> Unit,
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = {
            if (onBackIconButtonClicked != null) {
                Rn3IconButton(
                    icon = Outlined.ArrowBack,
                    contentDescription = stringResource(string.core_designsystem_topAppBar_navigationIcon_iconButton_arrowBack_contentDescription),
                    onClick = onBackIconButtonClicked,
                )
            }
        },
        actions = {
            when (actions.size) {
                0 -> Unit
                1 -> actions[0].IconButton()
                else -> {
                    var expanded by remember { mutableStateOf(value = false) }

                    Rn3IconButton(
                        icon = Icons.Outlined.MoreVert,
                        contentDescription = stringResource(string.core_designsystem_topAppBar_actions_iconButton_moreVert_contentDescription),
                    ) { expanded = true }

                    actions.DropdownMenu(expanded = expanded) { expanded = false }
                }
            }
        },
        windowInsets = windowInsets,
        scrollBehavior = scrollBehavior,
        colors = if (transparent) {
            TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            )
        } else {
            TopAppBarDefaults.topAppBarColors()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Rn3SmallTopAppBar(
            title = { Text(text = "Preview default") },
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Rn3PreviewComponentVariation
@Composable
private fun BackArrow() {
    Rn3Theme {
        Rn3SmallTopAppBar(
            title = { Text(text = "Preview back button") },
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        )
    }
}
