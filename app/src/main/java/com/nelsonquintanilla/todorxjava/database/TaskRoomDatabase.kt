package com.nelsonquintanilla.todorxjava.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nelsonquintanilla.todorxjava.model.TaskItem
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Date

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
                        val taskDatabase = database ?: return
                        taskDatabase.taskDao().insertTasks(
                            listOf(
                                TaskItem(
                                    id = null,
                                    text = "Chapter 1: Hello, RxJava!",
                                    addedDate = Date(),
                                    isDone = false
                                ),
                                TaskItem(
                                    id = null,
                                    text = "Chapter 2: Observables",
                                    addedDate = Date(),
                                    isDone = false
                                ),
                                TaskItem(
                                    id = null,
                                    text = "Chapter 3: Subjects",
                                    addedDate = Date(),
                                    isDone = false
                                ),
                                TaskItem(
                                    id = null,
                                    text = "Chapter 4: Observables and Subjects in practice",
                                    addedDate = Date(),
                                    isDone = false
                                ),
                                TaskItem(
                                    id = null,
                                    text = "Chapter 5: Filtering operators",
                                    addedDate = Date(),
                                    isDone = false
                                ),
                            )
                        )
                            .subscribeOn(Schedulers.io())
                            .subscribe()
                    }
                })
                .build()
            database = localDatabase
            localDatabase
        }
    }
}