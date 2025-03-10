/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sramky.learn.omdb.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import sramky.learn.omdb.data.local.database.OmdbApi
import sramky.learn.omdb.data.local.database.OmdbApiDao

/**
 * Unit tests for [DefaultOmdbApiRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultOmdbApiRepositoryTest {

    @Test
    fun omdbApis_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultOmdbApiRepository(FakeOmdbApiDao())

        repository.add("Repository")

        assertEquals(repository.omdbApis.first().size, 1)
    }

}

private class FakeOmdbApiDao : OmdbApiDao {

    private val data = mutableListOf<OmdbApi>()

    override fun getOmdbApis(): Flow<List<OmdbApi>> = flow {
        emit(data)
    }

    override suspend fun insertOmdbApi(item: OmdbApi) {
        data.add(0, item)
    }
}
