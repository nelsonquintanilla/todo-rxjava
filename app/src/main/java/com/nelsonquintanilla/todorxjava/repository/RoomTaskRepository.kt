package com.nelsonquintanilla.todorxjava.repository

import com.nelsonquintanilla.todorxjava.database.TaskDatabase
import com.nelsonquintanilla.todorxjava.model.TaskItem
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class RoomTaskRepository(private val database: TaskDatabase) : TaskRepository {

    companion object {
        const val INVALID_ID = -1
    }

    override fun insertTask(taskItem: TaskItem): Single<Long> {
        val validIdTask =
            if (taskItem.id == INVALID_ID) {
                taskItem.copy(id = null)
            } else {
                taskItem
            }
        return database.taskDao().insertTask(taskItem = validIdTask)
    }

    override fun getTask(id: Int): Maybe<TaskItem> {
        return database.taskDao().fetchTask(id = id)
    }

    override fun taskStream(): Observable<List<TaskItem>> {
        return database.taskDao().taskStream()
    }
}
