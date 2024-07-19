package com.nelsonquintanilla.todorxjava.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.nelsonquintanilla.todorxjava.database.TaskRoomDatabase
import com.nelsonquintanilla.todorxjava.databinding.ActivityEditTodoBinding
import com.nelsonquintanilla.todorxjava.repository.RoomTaskRepository
import com.nelsonquintanilla.todorxjava.utils.buildViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTodoBinding
    private val disposables = CompositeDisposable()
    private lateinit var viewModel: EditTaskViewModel

    companion object {
        private const val TASK_ID_KEY = "taskIdKey"
        fun launch(context: Context, taskId: Int) {
            val intent = Intent(context, EditTaskActivity::class.java)
            intent.putExtra(TASK_ID_KEY, taskId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeView()
        initializeViewModel()
        setupObservers()
        setupListeners()
    }

    private fun initializeView() {
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initializeViewModel() {
        viewModel = buildViewModel {
            val repository = RoomTaskRepository(TaskRoomDatabase.fetchDatabase(this))
            val taskIdKey = intent.getIntExtra(TASK_ID_KEY, RoomTaskRepository.INVALID_ID)
            EditTaskViewModel(repository, Schedulers.io(), taskIdKey)
        }
    }

    private fun setupObservers() {
        viewModel.finishLiveData.observe(this) { finish() }
        viewModel.textLiveData.observe(this) { binding.titleInput.append(it) }
    }

    private fun setupListeners() {
        // RxBinding method to listen for input event and forward it to the view model
        binding.done.clicks()
            .subscribe { viewModel.onFinishedClicked() }
            .addTo(disposables)

        // RxBinding method to listen for input event and forward it to the view model
        binding.titleInput.textChanges()
            .subscribe { viewModel.onTextChanged(it) }
            .addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}