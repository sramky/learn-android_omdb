# learn-android_omdb
Repository for training application using OMDb API

Kľúč pre OMDb API je v kóde načítaný z BuildConfig. Uložený je v gradle.properties vo fielde "OMDB_API_KEY".
Knižnice sú riešené pomocou libs.
Error handling som chcel riešiť v dvoch premenných errorMessage a errorMessage2.
    → Chcel som oddeliť hlášku z json odpovede a hlášku z http.
    → Napr. v prípade zlého kľúča - Máme dve rozdielne hlášky: "HTTP 401 " a z JSON: "Invalid API key!". V takomto prípade v našej aplikácii ale dostávame response inak ako v prípade prehliadača.

Aplikácia používa:
    → Jetpack Compose pre UI
    → Retrofit na vytvorenie HTTP Clienta
    → GSON na parsovanie JSON
    → Coroutines na asynchrónne volanie requestov
    → MVVM architektúru a StateFlow na uchovanie hodnôt premenných pri zániknu inštancie
    → Toast na zobrazenie chýb pri Exception
    → Aplikácia má responzívny dizajn

Adresárová štruktúra:
    → network
        - ApiClient         (Singleton objekt ApiClient. Konfigurácia Retrofit klienta pre komunikáciu s OMDB API.)
        - OmdbApiService    (Retrofit rozhranie s asynchrónnou funkciou getMovies pre volanie OMDB API.)
        - OmdbResponse      (Dátové triedy pre odpoveď.)
    → ui
        → viewmodel
            - OmdbViewModel (Implementácia MVVM architektúry)
        - MainActivity      (Základné UI v Jetpack Compose)
        - uiComponent       (Definícia funkcií pre tlačítka a textové polia)