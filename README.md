## BookStore Android Application

A modern Android bookstore application built with Kotlin, implementing MVVM architecture with reactive data flow using StateFlow and Room database integration.

### Features

- **Book Discovery**: Browse featured books, categories, and new arrivals on the home screen
- **Advanced Search**: Search books with filters for category, price range, and keywords
- **Shopping Cart**: Add books to cart with quantity management
- **User Authentication**: Login/register system with JWT token management
- **Book Details**: Detailed view of individual books with purchase options

### Architecture

The application follows modern Android development practices:

- **MVVM Pattern**: [1](#0-0) 
- **Single Activity Architecture**: 
- **Repository Pattern**: 
- **Reactive UI**: StateFlow for reactive data binding and lifecycle-aware observers

### Tech Stack

- **Language**: Kotlin
- **UI**: Fragments with ViewBinding, Material Design 3 components
- **Architecture**: MVVM with Repository pattern
- **Networking**: Retrofit with OkHttp interceptors for authentication
- **Database**: Room for local data persistence
- **Image Loading**: Glide for book cover images
- **Reactive Programming**: Kotlin Coroutines and StateFlow

### Key Components

#### Home Screen
- **Featured Books**: Horizontal scrolling list of recommended books
- **Categories**: Grid view of book genres for easy browsing
- **New Arrivals**: Recently added books in a 2-column grid layout  

#### Search Functionality
- **Advanced Filters**: Search by title, author, category, and price range
- **Dynamic Results**: Real-time search results with error handling 

#### Data Management
- **Concurrent Loading**: Parallel API calls for optimal performance
- **Error Handling**: Comprehensive error states with user feedback
- **Offline Support**: Room database for local data caching

### API Integration

The app connects to a backend server running on `http://10.0.2.2:3000/` with JWT authentication: 

### Getting Started

1. Clone the repository
2. Open in Android Studio
3. Ensure backend server is running on local host for emulator use `10.0.2.2:3000`
4. Build and run the application

### Project Structure

```
app/src/main/java/com/example/onlinebookstoreapp/
├── fragments/          # UI fragments (Home, Search, Cart, etc.)
├── viewmodel/         # ViewModels for MVVM architecture
├── repository/        # Data repository layer
├── auth/             # Authentication management
├── Entities/         # Data models
└── model/            # API response models
```

