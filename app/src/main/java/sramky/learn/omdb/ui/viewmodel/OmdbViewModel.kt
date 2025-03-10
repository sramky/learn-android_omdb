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
package sramky.learn.omdb.ui.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sramky.learn.omdb.network.ApiClient
import sramky.learn.omdb.network.Movie

class OmdbViewModel : ViewModel() {
    // StateFlow
    private val _apiKey = MutableStateFlow("db9d55bc")
    val apiKey: StateFlow<String> = _apiKey
    private val _enteredMovieTitle = MutableStateFlow("")
    val enteredMovieTitle: StateFlow<String> = _enteredMovieTitle
    private val _searchTrigger = MutableStateFlow(false)
    val searchTrigger: StateFlow<Boolean> = _searchTrigger
/*
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies
*/
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val _errorMessage2 = MutableStateFlow<String?>(null)
    val errorMessage2: StateFlow<String?> = _errorMessage2

    // Funkcia na spustenie vyhľadávania
    fun searchMovies(context: Context, apiKey: String, enteredMovieTitle: String) {
        _searchTrigger.value = true
        _errorMessage.value = null
        _errorMessage2.value = null

        if (_enteredMovieTitle.value.isNotEmpty()) {

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = ApiClient.apiService.getMovies(apiKey, enteredMovieTitle)
                    Log.d("OMDB_RESPONSE", "API Response: $response") // Log the entire response

                    if (response.response == "True") {
                        _movies.value = response.search ?: emptyList()
                        _count.value = response.totalResults
                        _errorMessage.value = null
                        _errorMessage2.value = null
                    } else {
                        _errorMessage.value = response.error ?: "Unknown error"
                        _errorMessage2.value = response.error ?: "Unknown error"
                        _movies.value = emptyList()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _errorMessage2.value = "Error: ${e.message}"
                        _errorMessage.value =
                            if (_errorMessage2.value == "Error: HTTP 401 ") "Invalid API key!" else "Error: ${e.message}"
                        _movies.value = emptyList()
                        Log.e("OMDB_ERROR", "Error fetching data: ${e.message}")
                        //Log.e("OMDB_ERROR", "msg:  ${errorMessage} ; msg2: ${errorMessage2}")
                        Toast.makeText(context, _errorMessage2.value, Toast.LENGTH_SHORT).show()
                    }
                } finally {
                    _searchTrigger.value = false
                }
            }
        }
    }
    fun updateApiKey(key: String){
        _apiKey.value = key
    }

    fun updateEnteredMovieTitle(title: String) {
        _enteredMovieTitle.value = title
    }
}