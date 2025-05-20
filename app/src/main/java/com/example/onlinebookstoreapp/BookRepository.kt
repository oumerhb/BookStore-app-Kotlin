// BookRepository.kt
package com.example.onlinebookstoreapp

object BookRepository {
    // Consolidate all book definitions here
    val allBooks: List<Book> = listOf(
        Book("1", "The Midnight Library", "Matt Haig", price = "$10.99", category = "Romance", imageUrl = "placeholder_image_simple_x"),
        Book("2", "Klara and the Sun", "Kazuo Ishiguro", price = "$12.50", category = "Science", imageUrl = "placeholder_image_simple_x"),
        Book("3", "Project Hail Mary", "Andy Weir", price = "$11.00", category = "Science", imageUrl = "placeholder_image_simple_x"),
        Book("4", "Atomic Habits", "James Clear", price = "$9.99", category = "Design", imageUrl = "placeholder_image_simple_x"),
        Book("5", "The Vanishing Half", "Brit Bennett", price = "$14.00", category = "Romance", imageUrl = "placeholder_image_simple_x"),
        Book("6", "Where the Crawdads Sing", "Delia Owens", price = "$8.50", category = "Thriller", imageUrl = "placeholder_image_simple_x"),
        Book("7", "Another Great Book", "Some Author", price = "$13.20", category = "Kids", imageUrl = "placeholder_image_simple_x"),
        Book("8", "Old Classic", "Famous Writer", price = "$5.00 (Sale)", category = "Design", imageUrl = "placeholder_image_simple_x"),
        Book("9", "Hidden Gem", "New Talent", price = "$4.50 (Sale)", category = "Kids", imageUrl = "placeholder_image_simple_x"),
        // From BookDetailsActivity dummy data
        Book("dm", "Don't Make Me Think", "Steve Krug", price = "EGP 35.46", category = "Web design", imageUrl = "dont_make_me_think_cover"),
        // From library dummy data
        Book("dj", "Design is a Job", "Mike Monteiro", category = "Web design", imageUrl = "design_is_a_job_cover", price = "EGP 190"),
        Book("dw", "Designing with Web Standards", "Jeffrey Zeldman", category = "Web design", imageUrl = "designing_with_web_standards_cover", price = "EGP 160"),
        Book("s1", "JavaScript and JQuery", "Jon Duckett", category = "Web design", price = "EGP 180", imageUrl="js_jquery_cover"),
        Book("s2", "Responsive Web Design", "Ethan Marcotte", category = "Web design", price = "EGP 150", imageUrl="responsive_web_cover"),
        Book("s3", "Neuro Web Design", "Susan Weinschenk", category = "Web design", price = "EGP 170", imageUrl="neuro_web_cover")
    )

    fun getBookById(bookId: String): Book? {
        return allBooks.find { it.id == bookId }
    }

    fun getBooksByIds(ids: Collection<String>): List<Book> {
        return allBooks.filter { ids.contains(it.id) }
    }
}