package com.example.project.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project.room.dao.OperationsDao
import com.example.project.room.entity.*;
import java.io.File

@Database(entities = [Category::class,Currency::class,Type::class,Operation::class,Bill::class], version = 1)
abstract class OperationsDatabase : RoomDatabase() {
    abstract fun OperationsDao():OperationsDao
    companion object{
        private var INSTANCE: OperationsDatabase? = null
        fun getDatabase(context: Context): OperationsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OperationsDatabase::class.java,
                    "word_database.db"
                ).allowMainThreadQueries().createFromAsset("asset.db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}