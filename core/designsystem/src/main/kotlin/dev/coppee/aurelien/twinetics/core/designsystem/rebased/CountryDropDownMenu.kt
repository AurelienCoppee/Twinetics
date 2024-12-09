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

package dev.coppee.aurelien.twinetics.core.designsystem.rebased

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3OutlinedTextField
import dev.coppee.aurelien.twinetics.core.model.data.Country

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Rn3CountryDropDownMenu(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (Country) -> Unit,
    label: @Composable (() -> Unit)? = null,
    hasUserInteracted: Boolean = false,
    beEmpty: Boolean = false,
    textItem: @Composable (Country) -> Unit,
    setIsFocused: (Boolean) -> Unit,
    autofill: AutofillType? = null,
    enableAutofill: Boolean = false,
) {
    var expanded by remember { mutableStateOf(value = false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        Rn3OutlinedTextField(
            readOnly = true,
            value = value,
            onValueChange = {},
            label = label,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    setIsFocused(focusState.isFocused)
                },
            hasUserInteracted = hasUserInteracted,
            enableAutofill = enableAutofill,
            autofillTypes = autofill,
            beEmpty = beEmpty,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Country.sortedCountries().forEach { selected ->
                DropdownMenuItem(
                    text = { textItem(selected) },
                    onClick = {
                        expanded = false
                        onValueChange(selected)
                    },
                )
            }
        }
    }
}
