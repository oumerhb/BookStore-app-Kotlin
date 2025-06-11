package com.example.onlinebookstoreapp

import com.example.onlinebookstoreapp.Entities.CategoryEntity

enum class BookGenre(val displayName: String) {
    FICTION("Fiction"),
    NON_FICTION("Non Fiction"),
    SCIENCE_FICTION("Science Fiction"),
    MYSTERY("Mystery"),
    THRILLER("Thriller"),
    COMEDY("Comedy"),
    ROMANCE("Romance"),
    HISTORY("History"),
    BIOGRAPHY("Biography"),
    SELF_HELP("Self Help"),
    PROGRAMMING("Programming"),
    FANTASY("Fantasy"),
    HORROR("Horror"),
    CLASSIC("Classic"),
    POETRY("Poetry"),
    YOUNG_ADULT("Young Adult"),
    HUMOR("Humor"),
    DYSTOPIA("Dystopia"),
    HISTORICAL_FICTION("Historical Fiction"),
    PSYCHOLOGY("Psychology"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology"),
    ENGINEERING("Engineering"),
    MATHEMATICS("Mathematics"),
    PHILOSOPHY("Philosophy"),
    TRAVEL("Travel"),
    ART("Art"),
    MUSIC("Music"),
    SPORTS("Sports"),
    NATURE("Nature"),
    ECONOMICS("Economics"),
    POLITICS("Politics"),
    HEALTH("Health"),
    EDUCATION("Education"),
    LITERARY_CRITICISM("Literary Criticism"),
    JOURNALISM("Journalism"),
    TRUE_CRIME("True Crime"),
    MEMOIR("Memoir"),
    SOCIOLOGY("Sociology");

    companion object {
        fun getAllGenres(): List<CategoryEntity> {
            return entries.map { genre ->
                CategoryEntity(
                    id = genre.name, // Using enum name as ID
                    name = genre.displayName,
                    bookCount = 0, // Default to 0 since we don't have actual counts
                    imageUrl = "", // Default empty since we don't have image URLs
                    lastUpdated = System.currentTimeMillis()
                )
            }
        }
    }
}