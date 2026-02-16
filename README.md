Android application for tracking real-time currency exchange rates using [Frankfurter API](https://www.frankfurter.app/).

##  Features

- View current exchange rates with selectable base currency
- Add currency pairs to favorites
- Sort currencies by code (A-Z, Z-A) or rate (ascending, descending)
- Pull-to-refresh for rate updates
- Offline currency list caching
- Error handling with retry

##  Architecture

**Clean Architecture** with 3 layers:
- **Data Layer**: Retrofit (API), Room (cache), DataStore (settings)
- **Domain Layer**: Business logic, Repository interface
- **Presentation Layer**: MVVM + UDF, Jetpack Compose + XML

##  Tech Stack

- **Kotlin** - 100%
- **Jetpack Compose** - Modern declarative UI
- **Coroutines + Flow** - Asynchronous operations
- **Hilt** - Dependency Injection
- **Room** - Local database
- **Retrofit** - Network client
- **Navigation Component** - Fragment navigation
- **DataStore** - Preferences storage

##  Key Implementation Details

### Custom Dropdown
- Unified border between field and menu
- Manual border drawing with `drawWithContent`
- Shadow and rounded corners support

### Network Optimization
- Parallel API requests with `async/await`
- Grouped requests by base currency (50 pairs → 5-10 requests)
- Mutex for preventing race conditions

### Navigation
- Bottom menu without back stack (global actions)
- Proper back stack for Sort screen

##  License
This project is a test assignment.
