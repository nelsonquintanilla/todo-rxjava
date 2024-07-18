package com.nelsonquintanilla.todorxjava.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nelsonquintanilla.todorxjava.model.TaskItem
import com.nelsonquintanilla.todorxjava.repository.RoomTaskRepository
import com.nelsonquintanilla.todorxjava.repository.TaskRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TodoListViewModel(
    repository: TaskRepository,
    backgroundScheduler: Scheduler,
    // Best practice: pass in dedicated Scheduler to use for timing tasks, that way it's possible to
    // advance time manually using a TestScheduler in unit tests
    computationScheduler: Scheduler,
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val taskClicks = PublishSubject.create<TaskItem>()
    private val taskDoneToggles = PublishSubject.create<Pair<TaskItem, Boolean>>() // Observable listening for a tap on any of the task items

    // Int represents the id of the task item to be edited. It's best practice to pass around the
    // smallest piece of data possible between 2 activities so that the maximum amount of information
    // an intent can carry is not exceeded.
    val showEditTaskLiveData = MutableLiveData<Int>()
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

        // Save updated version of the TaskItem the user toggled
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

        // Indicate that the activity should launch the edit task activity
        taskClicks
            // To ensure that only 1 tap goes through: immediately emits an item and then skips any
            // new items that come within the designated time period. Ensures that multiple activities
            // aren't started by quickly tapping on a task.
            .throttleFirst(1, TimeUnit.SECONDS, computationScheduler)
            .subscribe {
                val id = it.id ?: RoomTaskRepository.INVALID_ID
                showEditTaskLiveData.postValue(id)
            }
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