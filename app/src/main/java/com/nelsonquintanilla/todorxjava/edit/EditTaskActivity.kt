package com.nelsonquintanilla.todorxjava.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel = buildViewModel {
            val repository = RoomTaskRepository(TaskRoomDatabase.fetchDatabase(this))
            val taskIdKey = intent.getIntExtra(TASK_ID_KEY, RoomTaskRepository.INVALID_ID)
            EditTaskViewModel(repository, Schedulers.io(), taskIdKey)
        }

        // RxBinding method to listen for input event and forward it to the view model
        binding.done.clicks()
            .subscribe { viewModel.onFinishedClicked() }
            .addTo(disposables)

        // RxBinding method to listen for input event and forward it to the view model
        binding.titleInput.textChanges()
            .subscribe { viewModel.onTextChanged(it) }
            .addTo(disposables)

        viewModel.finishLiveData.observe(this, Observer { finish() })

        viewModel.textLiveData.observe(this, Observer(binding.titleInput::append))
    }
}