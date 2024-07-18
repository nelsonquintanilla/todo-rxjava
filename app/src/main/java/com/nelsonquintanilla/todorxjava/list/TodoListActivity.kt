package com.nelsonquintanilla.todorxjava.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.nelsonquintanilla.todorxjava.database.TaskRoomDatabase
import com.nelsonquintanilla.todorxjava.databinding.ActivityTodoListBinding
import com.nelsonquintanilla.todorxjava.edit.EditTaskActivity
import com.nelsonquintanilla.todorxjava.repository.RoomTaskRepository
import com.nelsonquintanilla.todorxjava.utils.buildViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class TodoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoListBinding
    private val disposables = CompositeDisposable() // To responsibly dispose of observable chains

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val adapter = TodoAdapter()
        binding.todoList.layoutManager = LinearLayoutManager(this)
        binding.todoList.adapter = adapter

        val viewModel = buildViewModel {
            val repository = RoomTaskRepository(TaskRoomDatabase.fetchDatabase(this))
            TodoListViewModel(repository, Schedulers.io(), Schedulers.computation())
        }

        binding.addButton.clicks()
            .subscribe { viewModel.addClicked() }
            .addTo(disposables)

        viewModel.listItemsLiveData
            .observe(this, Observer(adapter::submitList))

        // Subscribe to observables defined in adapter and forward the result to the viewmodel
        // This is because if we pass the observables defined in adapter directly to the viewmodel,
        // when the user rotates the screen the viewmodel would stop receiving callbacks, since the
        // adapter would create new PublishSubjects which the viewmodel would not know about
        adapter.taskClickStream.subscribe {
            viewModel.taskClicked(it)
        }.addTo(disposables)

        adapter.taskToggledStream.subscribe {
            viewModel.taskDoneToggled(it.first, it.second)
        }.addTo(disposables)

        viewModel.showEditTaskLiveData.observe(this, Observer {
            EditTaskActivity.launch(context = this, taskId = it)
        })
    }
}