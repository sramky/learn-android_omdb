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

package sramky.learn.omdb.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import sramky.learn.omdb.ui.viewmodel.OmdbViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieSearchScreen()
        }
    }

    @Composable
    fun MovieSearchScreen() {

        val viewModel: OmdbViewModel = viewModel()

        val apiKey by viewModel.apiKey.collectAsState()
        val enteredMovieTitle by viewModel.enteredMovieTitle.collectAsState()
        val searchTrigger by viewModel.searchTrigger.collectAsState()
        val movies by viewModel.movies.collectAsState()
        val count by viewModel.count.collectAsState()
        val errorMessage by viewModel.errorMessage.collectAsState()
        val errorMessage2 by viewModel.errorMessage2.collectAsState()

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // textové polia pre zadanie api kľúča a hľadaného výrazu

            EditableOutlinedTextField(
                value = apiKey,
                onValueChange = { viewModel.updateApiKey(it) },
                label = "Enter your API Key"
            )

            EditableOutlinedTextField(
                value = enteredMovieTitle,
                onValueChange = { viewModel.updateEnteredMovieTitle(it) },
                label = "Enter movie title"
            )

            val finalUrl = "http://www.omdbapi.com/?apikey=$apiKey&s=$enteredMovieTitle"
            Log.d("OMDB_URL", "Request URL: $finalUrl")

            // Tlačítko
            TapButton(
                buttonText = if (searchTrigger) "Loading..." else "Search",
                enabled = !searchTrigger,
                onClick = { viewModel.searchMovies(context, apiKey, enteredMovieTitle) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Výpis chybových hlášok
            if (errorMessage != null && errorMessage2 != null) {
                Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                if (errorMessage != errorMessage2) {
                    Text(text = errorMessage2 ?: "", color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Výpis výsledkov
            if (movies.isNotEmpty()) {
                Text(text = "Počet vyhľadaných filmov: $count")
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn (horizontalAlignment = Alignment.CenterHorizontally) {
                    items(movies) { movie ->
                        Text(text= "${movie.title} (${movie.year})")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            } else if (searchTrigger)
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    strokeWidth = 4.dp
                )

        }
    }
}