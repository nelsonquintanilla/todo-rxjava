package com.nelsonquintanilla.todorxjava.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object TaskRoomDatabase {
    private var database: TaskDatabase? = null

    fun fetchDatabase(context: Context): TaskDatabase {
        val localDatabaseCopy = database
        return if (localDatabaseCopy != null) {
            localDatabaseCopy
        } else {
            val localDatabase = Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java, "book_database"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    @SuppressLint("CheckResult")
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        TODO("Add default items")
                    }
                })
                .build()
            database = localDatabase
            localDatabase
        }
    }
}