package com.nelsonquintanilla.todorxjava.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.nelsonquintanilla.todorxjava.R
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
    private lateinit var viewModel: TodoListViewModel
    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeView()
        initializeViewModel()
        setupRecyclerView()
        setupSwipeToRemove()
        setupUI()
        observeViewModel()
        observeAdapterEvents()
    }

    private fun initializeView() {
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initializeViewModel() {
        viewModel = buildViewModel {
            val repository = RoomTaskRepository(TaskRoomDatabase.fetchDatabase(this))
            TodoListViewModel(repository, Schedulers.io(), Schedulers.computation())
        }
    }

    private fun setupRecyclerView() {
        adapter = TodoAdapter()
        binding.todoList.apply {
            layoutManager = LinearLayoutManager(this@TodoListActivity)
            this.adapter = this@TodoListActivity.adapter
        }
    }

    private fun setupSwipeToRemove() {
        val swipeToRemoveHelper = SwipeToRemoveHelper(adapter)
        ItemTouchHelper(swipeToRemoveHelper).attachToRecyclerView(binding.todoList)

        swipeToRemoveHelper.swipeStream
            .subscribe { viewModel.taskSwiped(it) }
            .addTo(disposables)
    }

    private fun setupUI() {
        binding.statistics.visibility = View.VISIBLE
        binding.addButton.clicks()
            .subscribe { viewModel.addClicked() }
            .addTo(disposables)
    }

    private fun observeViewModel() {
        viewModel.statisticsLiveData.observe(this) { (done, due) ->
            binding.statistics.text = getString(R.string.statistics_information, done, due)
        }

        viewModel.listItemsLiveData.observe(this) { items ->
            adapter.submitList(items)
        }

        viewModel.showEditTaskLiveData.observe(this) { taskId ->
            EditTaskActivity.launch(context = this, taskId = taskId)
        }

        observeAdapterEvents()
    }

    private fun observeAdapterEvents() {
        // Subscribe to observables defined in adapter and forward the result to the viewmodel
        // This is because if we pass the observables defined in adapter directly to the viewmodel,
        // when the user rotates the screen the viewmodel would stop receiving callbacks, since the
        // adapter would create new PublishSubjects which the viewmodel would not know about
        adapter.taskClickStream
            .subscribe { viewModel.taskClicked(it) }
            .addTo(disposables)

        adapter.taskToggledStream
            .subscribe { viewModel.taskDoneToggled(it.first, it.second) }
            .addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}