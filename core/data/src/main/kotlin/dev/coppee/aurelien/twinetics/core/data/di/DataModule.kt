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

package dev.coppee.aurelien.twinetics.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.coppee.aurelien.twinetics.core.data.repository.linksRn3UrlData.FirestoreLinksRn3UrlDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.linksRn3UrlData.LinksRn3UrlDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.sensorData.OfflineFirstSensorDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.sensorData.SensorDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.userData.OfflineFirstUserDataRepository
import dev.coppee.aurelien.twinetics.core.data.repository.userData.UserDataRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository,
    ): UserDataRepository

    @Binds
    internal abstract fun bindsSensorDataRepository(
        sensorDataRepository: OfflineFirstSensorDataRepository,
    ): SensorDataRepository

    @Binds
    internal abstract fun bindsLinksRn3UrlDataRepository(
        linksRn3UrlDataRepository: FirestoreLinksRn3UrlDataRepository,
    ): LinksRn3UrlDataRepository
}