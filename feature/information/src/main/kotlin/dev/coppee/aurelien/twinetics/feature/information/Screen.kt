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

package dev.coppee.aurelien.twinetics.feature.information

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.coppee.aurelien.twinetics.core.common.Rn3Uri
import dev.coppee.aurelien.twinetics.core.common.toRn3Uri
import dev.coppee.aurelien.twinetics.core.config.LocalConfigHelper
import dev.coppee.aurelien.twinetics.core.designsystem.TopAppBarAction
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3ExpandableSurface
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3LargeButton
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3OutlinedTextField
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3Scaffold
import dev.coppee.aurelien.twinetics.core.designsystem.component.Rn3TextDefaults
import dev.coppee.aurelien.twinetics.core.designsystem.component.TopAppBarStyle.HOME
import dev.coppee.aurelien.twinetics.core.designsystem.component.tile.Rn3TileUri
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3AdditionalPadding
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValues
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValuesDirection.HORIZONTAL
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.padding
import dev.coppee.aurelien.twinetics.core.designsystem.rebased.Rn3CountryDropDownMenu
import dev.coppee.aurelien.twinetics.core.designsystem.rebased.Rn3PhoneForm
import dev.coppee.aurelien.twinetics.core.designsystem.rebased.icon
import dev.coppee.aurelien.twinetics.core.designsystem.rebased.text
import dev.coppee.aurelien.twinetics.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.coppee.aurelien.twinetics.core.feedback.navigateToFeedback
import dev.coppee.aurelien.twinetics.core.model.data.AddressInfo
import dev.coppee.aurelien.twinetics.core.model.data.Country
import dev.coppee.aurelien.twinetics.core.ui.TrackScreenViewEvent
import dev.coppee.aurelien.twinetics.feature.information.model.InformationUiState.Loading
import dev.coppee.aurelien.twinetics.feature.information.model.InformationUiState.Success
import dev.coppee.aurelien.twinetics.feature.information.model.InformationViewModel
import dev.coppee.aurelien.twinetics.feature.information.model.data.InformationData
import kotlinx.coroutines.delay


@Composable
internal fun InformationRoute(
    modifier: Modifier = Modifier,
    viewModel: InformationViewModel = hiltViewModel(),
    navController: NavController,
    navigateToNextPage: () -> Unit,
) {
    val config = LocalConfigHelper.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        Loading -> {}
        is Success -> InformationScreen(
            modifier = modifier,
            data = (uiState as Success).informationData,
            feedbackTopAppBarAction = FeedbackScreenContext(
                localName = "ConnectScreen",
                localID = "oujWHHHpuFbChUEYhyGX39V2exJ299Dw",
            ).toTopAppBarAction(navController::navigateToFeedback),
            onValidateLocationClicked = { address, phone ->
                viewModel.save(address, phone)
                navigateToNextPage()
            },
            privacyPolicyTileUri = config.getString("privacy_policy_url").toRn3Uri {
            },
        )
    }

    TrackScreenViewEvent(screenName = "information")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun InformationScreen(
    modifier: Modifier = Modifier,
    data: InformationData,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onValidateLocationClicked: (AddressInfo, PhoneNumber?) -> Unit,
    privacyPolicyTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = "",
        topAppBarTitleAlignment = CenterHorizontally,
        onBackIconButtonClicked = null,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
        topAppBarStyle = HOME,
    ) {
        InformationForm(it, data, onValidateLocationClicked, privacyPolicyTileUri)
    }
}

@Composable
private fun InformationForm(
    paddingValues: Rn3PaddingValues,
    data: InformationData,
    onValidateLocationClicked: (AddressInfo, PhoneNumber?) -> Unit,
    privacyPolicyTileUri: Rn3Uri,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
    ) {
        var address by remember { mutableStateOf(value = data.address) }
        var phone by remember { mutableStateOf(value = data.phone) }

        var hasUserInteracted by rememberSaveable { mutableStateOf(value = false) }

        Rn3ExpandableSurface(
            content = {
                Icon(imageVector = Outlined.Info, contentDescription = null)
                Text(
                    text = stringResource(R.string.feature_information_expandableSurface_title),
                    modifier = Modifier.padding(
                        Rn3TextDefaults.paddingValues.only(HORIZONTAL),
                    ),
                )
            },
            expandedContent = {
                Column {
                    Text(
                        modifier = Modifier.padding(
                            Rn3TextDefaults.paddingValues.only(HORIZONTAL),
                        ),
                        text = stringResource(R.string.feature_information_expandableSurface_location),
                    )
                    Rn3TileUri(
                        title = stringResource(R.string.feature_information_expandableSurface_privacyPolicy),
                        icon = Outlined.Policy,
                        uri = privacyPolicyTileUri,
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(Rn3AdditionalPadding.paddingValues.only(HORIZONTAL))) {
            AddressForm(
                address = address,
                onCountryChanged = { newCountry -> address = address.copy(country = newCountry) },
                onRegionChanged = { newRegion -> address = address.copy(region = newRegion) },
                onLocalityChanged = { newLocality ->
                    address = address.copy(locality = newLocality)
                },
                onPostalCodeChanged = { newPostalCode ->
                    address = address.copy(postalCode = newPostalCode)
                },
                onStreetChanged = { newStreet -> address = address.copy(street = newStreet) },
                onAuxiliaryDetailsChanged = { newAux ->
                    address = address.copy(auxiliaryDetails = newAux)
                },
                hasUserInteracted = hasUserInteracted,
            )

            Rn3PhoneForm(
                phone = phone ?: PhoneNumber(),
                onPhoneChanged = { updatedPhoneNumber -> phone = updatedPhoneNumber },
                hasUserInteracted = hasUserInteracted,
                beEmpty = true,
                keyboardOptionsNumber = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Rn3LargeButton(
                text = "Save",
                icon = Outlined.Check,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                hasUserInteracted = true
                if (address.country != null && address.street.isNotEmpty() && address.locality.isNotEmpty()) {
                    onValidateLocationClicked(address, phone)
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AddressForm(
    address: AddressInfo,
    onCountryChanged: (Country) -> Unit,
    onRegionChanged: (String) -> Unit,
    onLocalityChanged: (String) -> Unit,
    onPostalCodeChanged: (String) -> Unit,
    onStreetChanged: (String) -> Unit,
    onAuxiliaryDetailsChanged: (String) -> Unit,
    hasUserInteracted: Boolean = false,
) {
    var isFocusedPostalCode by rememberSaveable { mutableStateOf(value = false) }
    var showFullLabelPostalCode by rememberSaveable { mutableStateOf(value = false) }

    LaunchedEffect(isFocusedPostalCode) {
        if (isFocusedPostalCode) {
            delay(timeMillis = 50)
            showFullLabelPostalCode = true
        } else {
            showFullLabelPostalCode = false
        }
    }

    Column {
        Rn3CountryDropDownMenu(
            value = address.country?.text() ?: "",
            label = { Text(text = stringResource(R.string.feature_information_dropDownMenu_country)) },
            textItem = { phoneCode ->
                Row(verticalAlignment = CenterVertically) {
                    Icon(
                        imageVector = phoneCode.icon(),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified,
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(text = (phoneCode.text()))
                }
            },
            setIsFocused = { },
            hasUserInteracted = hasUserInteracted,
            enableAutofill = true,
            autofill = AutofillType.PhoneCountryCode,
            onValueChange = { newCountry ->
                onCountryChanged(newCountry)
            },
            beEmpty = false,
        )

        Rn3OutlinedTextField(
            value = address.region ?: "",
            onValueChange = { newRegion -> onRegionChanged(newRegion) },
            hasUserInteracted = hasUserInteracted,
            maxCharacters = 100,
            label = { Text(text = stringResource(R.string.feature_information_textField_region)) },
            beEmpty = true,
            singleLine = true,
            enableAutofill = true,
            autofillTypes = AutofillType.AddressRegion,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Rn3OutlinedTextField(
                modifier = Modifier.weight(1.5f),
                value = address.locality,
                onValueChange = { newLocality ->
                    onLocalityChanged(newLocality)
                },
                hasUserInteracted = hasUserInteracted,
                maxCharacters = 100,
                label = { Text(text = stringResource(R.string.feature_information_textField_locality)) },
                singleLine = true,
                enableAutofill = true,
                autofillTypes = AutofillType.AddressLocality,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
            )

            Rn3OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isFocusedPostalCode = focusState.isFocused
                    },
                value = address.postalCode ?: "",
                onValueChange = { newPostalCode ->
                    onPostalCodeChanged(newPostalCode)
                },
                maxCharacters = 20,
                label = {
                    Text(
                        text = if (showFullLabelPostalCode || address.postalCode != "") stringResource(
                            R.string.feature_information_textField_postalCode_full,
                        ) else stringResource(R.string.feature_information_textField_postalCode_small),
                    )
                },
                beEmpty = true,
                singleLine = true,
                enableAutofill = true,
                autofillTypes = AutofillType.PostalCode,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
            )
        }

        Rn3OutlinedTextField(
            value = address.street,
            onValueChange = { newStreet -> onStreetChanged(newStreet) },
            hasUserInteracted = hasUserInteracted,
            maxCharacters = 200,
            label = { Text(text = stringResource(R.string.feature_information_textField_street)) },
            singleLine = false,
            enableAutofill = true,
            autofillTypes = AutofillType.AddressStreet,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        )

        Rn3OutlinedTextField(
            value = address.auxiliaryDetails ?: "",
            onValueChange = { newAuxiliaryDetails ->
                onAuxiliaryDetailsChanged(newAuxiliaryDetails)
            },
            maxCharacters = 200,
            label = { Text(text = stringResource(R.string.feature_information_textField_auxiliaryDetails)) },
            hasUserInteracted = hasUserInteracted,
            beEmpty = true,
            singleLine = false,
            enableAutofill = true,
            autofillTypes = AutofillType.AddressAuxiliaryDetails,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        )
    }
}
