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

package dev.coppee.aurelien.twinetics.core.datastore.di

import android.companion.CompanionDeviceManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import coppee.aurelien.twinetics.core.datastore.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.coppee.aurelien.twinetics.core.datastore.UserPreferencesSerializer
import dev.coppee.aurelien.twinetics.core.network.Dispatcher
import dev.coppee.aurelien.twinetics.core.network.Rn3Dispatchers
import dev.coppee.aurelien.twinetics.core.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    internal fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(Rn3Dispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(context = scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile(fileName = "RahNeil_N3:user_preferences.pb")
        }

    @Provides
    @Singleton
    fun provideCompanionDeviceManager(
        @ApplicationContext context: Context,
    ): CompanionDeviceManager {
        return context.getSystemService(CompanionDeviceManager::class.java)
    }
}
