package com.example.onlinebookstoreapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.onlinebookstoreapp.DAO.BookDao
import com.example.onlinebookstoreapp.DAO.CartDao
import com.example.onlinebookstoreapp.DAO.CategoryDao
import com.example.onlinebookstoreapp.Entities.BookEntity
import com.example.onlinebookstoreapp.Entities.CartItemEntity
import com.example.onlinebookstoreapp.Entities.CategoryEntity

@Database(
    entities = [BookEntity::class, CategoryEntity::class,CartItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun cartDao(): CartDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bookstore_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}