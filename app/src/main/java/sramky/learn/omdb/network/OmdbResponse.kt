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

package sramky.learn.omdb.network

import com.google.gson.annotations.SerializedName

// trieda objektov odpovedí
data class OmdbResponse(
    @SerializedName("Response") val response: String,
    @SerializedName("Search") val search: List<Movie>?,
    val totalResults: Int,
    @SerializedName("Error") val error: String?
)

data class Movie(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    val imdbID: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String
)