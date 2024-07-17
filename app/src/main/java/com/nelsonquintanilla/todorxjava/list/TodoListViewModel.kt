package com.nelsonquintanilla.todorxjava.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nelsonquintanilla.todorxjava.repository.RoomTaskRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class TodoListViewModel(
    repository: RoomTaskRepository,
    backgroundScheduler: Scheduler,
) : ViewModel() {

    private val disposables = CompositeDisposable()
    val listItemsLiveData = MutableLiveData<List<TodoListItem>>()

    init {
        repository
            .taskStream() // Observable<List<TaskItem>>
            .map { tasks -> tasks.map { TodoListItem.TaskListItem(it) } } // List<TodoListItem.TaskListItem>
            .map { listItems ->
                val finishedTasks = listItems.filter { it.taskItem.isDone }
                val todoTasks = listItems - finishedTasks.toSet()
                listOf(
                    TodoListItem.DueTasks,
                    *todoTasks.toTypedArray(),
                    TodoListItem.DoneTasks,
                    *finishedTasks.toTypedArray()
                )
            }
            .subscribeOn(backgroundScheduler)
            .subscribe(listItemsLiveData::postValue)
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}