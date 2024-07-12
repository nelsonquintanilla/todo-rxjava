package com.nelsonquintanilla.todorxjava.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nelsonquintanilla.todorxjava.model.TaskItem

@Database(entities = [TaskItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}