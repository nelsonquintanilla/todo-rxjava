package com.nelsonquintanilla.todorxjava.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nelsonquintanilla.todorxjava.model.TaskItem
import com.nelsonquintanilla.todorxjava.repository.TaskRepository
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.Date

class EditTaskViewModel(
    private val repository: TaskRepository,
    private val backgroundScheduler: Scheduler,
    private val taskId: Int,
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val finishedClicks = PublishSubject.create<Unit>()
    private val taskTitleTextChanges = BehaviorSubject.create<CharSequence>()

    val finishLiveData = MutableLiveData<Unit>()
    val textLiveData = MutableLiveData<String>()

    init {
        initializeStreams()
    }

    private fun initializeStreams() {
        // Get a Maybe<TaskItem> representing the TaskItem being edited
        // If there's no TaskItem that corresponds to the passed in id, the Maybe will emit nothing
        // and complete. cache() is to use existing task in multiple places without remaking the
        // call every time (since that could be expensive)
        val existingTask = repository.getTask(taskId).cache()

        initializeExistingTask(existingTask)
        handleTaskSaving(existingTask)
    }

    private fun initializeExistingTask(existingTask: Maybe<TaskItem>) {
        existingTask
            .subscribeOn(backgroundScheduler)
            .subscribe { textLiveData.postValue(it.text) }
            .addTo(disposables)
    }

    private fun handleTaskSaving(existingTask: Maybe<TaskItem>) {
        // Combine both observables to have the latest text whenever the done button is tapped
        // and save TaskItem in the database
        Observables.combineLatest(finishedClicks, taskTitleTextChanges)
            .map { it.second }
            // flatMapSingle() is used to convert from Observable to Single
            // Usually done when executing a network or database call that returns a Single after
            // some user interaction
            .flatMapSingle { title -> saveTask(existingTask, title) }
            .subscribe { finishLiveData.postValue(Unit) }
            .addTo(disposables)
    }

    private fun saveTask(existingTask: Maybe<TaskItem>, title: CharSequence): Single<Long> {
        return existingTask
            // Convert Maybe to Single.
            // If there's no TaskItem associated with the taskId passed into this view model,
            // we want to save a new TaskItem instead of modifying an existing one
            .defaultIfEmpty(createNewTask(title))
            .flatMap {
                val updatedTaskItem = it.copy(text = title.toString(), addedDate = Date())
                repository.insertTask(updatedTaskItem)
            }
            .subscribeOn(backgroundScheduler)
    }

    private fun createNewTask(title: CharSequence): TaskItem {
        return TaskItem(null, title.toString(), Date(), false)
    }

    fun onFinishedClicked() = finishedClicks.onNext(Unit)
    fun onTextChanged(text: CharSequence) = taskTitleTextChanges.onNext(text)

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}