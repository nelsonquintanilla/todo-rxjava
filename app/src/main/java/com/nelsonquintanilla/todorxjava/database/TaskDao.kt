package com.nelsonquintanilla.todorxjava.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nelsonquintanilla.todorxjava.model.TaskItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(taskItem: TaskItem): Single<Long> // Long represents the number of updated rows, which it's always expected to be 1

    // To add default tasks to the database.
    @Insert
    fun insertTasks(tasks: List<TaskItem>): Completable

    @Query("SELECT * FROM TaskItem WHERE id = :id")
    fun fetchTask(id: Int): Maybe<TaskItem> // Since there's no guarantee that a tasks exists for any given id, Maybe makes the most sense here

    // Observe all of the TaskItems currently in the database.
    // Every time a task is inserted or updated taskStream() should emit a new List<TaskItem> representing
    // all of the task items in the database.
    @Query("SELECT * FROM TaskItem ORDER BY addedDate")
    fun taskStream(): Observable<List<TaskItem>>
}