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

package dev.coppee.aurelien.twinetics.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import coppee.aurelien.twinetics.core.datastore.SensorList
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * An [androidx.datastore.core.Serializer] for the [SensorList] proto.
 */
class SensorSerializer @Inject constructor() : Serializer<SensorList> {
    override val defaultValue: SensorList = SensorList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SensorList =
        try {
            // readFrom is already called on the data store background thread
            SensorList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException(message = "Cannot read proto.", cause = exception)
        }

    override suspend fun writeTo(t: SensorList, output: OutputStream) {
        // writeTo is already called on the data store background thread
        t.writeTo(output)
    }
}
