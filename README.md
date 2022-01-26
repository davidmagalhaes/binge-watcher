# Binge Watcher
    An app to get information about series and its episodes. At first, aimed only on tv shows, 
    but who knows what comes next?

#### How to install
The app is available on [Play Store](https://play.google.com/store/apps/details?id=br.com.davidmag.bingewatcher)

#### Stack

- MVVM
- Clean Architecture
- RxJava2
- LiveData
- Kotlin
- Retrofit
- Room
- Stetho
- Paging Library 3
- Glide
- Dagger2
- Timber
- ThreetenBp

#### Features

- Support to configuration change (Screen rotation)
- Offline caching
- Infinite scrolling
- Search series by name

#### Bonus

- Favorites

#### Known issues
- On the first run, the app may fail to update the screen and get stuck on the placeholders. A simple pull-to-refresh should solve it.
- The app lacks a proper empty state view type.
- The episode list on the show details doesn't have a proper progress bar when loading.

#### Things to improve

- Adding Unit Tests / Espresso tests
- Some UX/UI improvements
- Adding infinite scrolling to the episodes.
