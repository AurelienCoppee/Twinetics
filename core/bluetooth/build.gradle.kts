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

plugins {
    alias(libs.plugins.rn3.android.library)
    alias(libs.plugins.rn3.android.library.compose)
    alias(libs.plugins.rn3.android.hilt)
}

android {
    namespace = "dev.coppee.aurelien.twinetics.core.bluetooth"
}

dependencies {
    implementation(libs.androidx.foundation.android)
    implementation(libs.activity.compose)

    implementation(projects.core.model)
}