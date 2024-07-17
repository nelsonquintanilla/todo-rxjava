package com.nelsonquintanilla.todorxjava.repository

import com.nelsonquintanilla.todorxjava.model.TaskItem
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface TaskRepository {
    fun insertTask(taskItem: TaskItem): Single<Long>
    fun getTask(id: Int): Maybe<TaskItem>
    fun taskStream(): Observable<List<TaskItem>>
}