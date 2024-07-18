package com.nelsonquintanilla.todorxjava.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nelsonquintanilla.todorxjava.model.TaskItem
import com.nelsonquintanilla.todorxjava.repository.RoomTaskRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject

class TodoListViewModel(
    repository: RoomTaskRepository,
    backgroundScheduler: Scheduler,
) : ViewModel() {

    private val disposables = CompositeDisposable()
    val listItemsLiveData = MutableLiveData<List<TodoListItem>>()

    private val taskClicks = PublishSubject.create<TaskItem>()
    private val taskDoneToggles = PublishSubject.create<Pair<TaskItem, Boolean>>() // Observable listening for a tap on any of the task items

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

        taskDoneToggles
            // flatMapSingle because flatMap expects the lambda to produce an Observable, but
            // repository.insertTask produces a Single
            .flatMapSingle { newItemPair ->
                repository
                    .insertTask(newItemPair.first.copy(isDone = newItemPair.second))
                    .subscribeOn(backgroundScheduler)
            }
            .subscribe()
            .addTo(disposables)
    }

    // Methods to forward events into PublishSubjects
    fun taskClicked(taskItem: TaskItem) {
        taskClicks.onNext(taskItem)
    }

    fun taskDoneToggled(taskItem: TaskItem, on: Boolean) {
        taskDoneToggles.onNext(Pair(taskItem, on))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}