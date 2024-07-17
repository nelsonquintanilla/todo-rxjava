package com.nelsonquintanilla.todorxjava.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.nelsonquintanilla.todorxjava.database.TaskRoomDatabase
import com.nelsonquintanilla.todorxjava.databinding.ActivityTodoListBinding
import com.nelsonquintanilla.todorxjava.repository.RoomTaskRepository
import com.nelsonquintanilla.todorxjava.utils.buildViewModel
import io.reactivex.rxjava3.schedulers.Schedulers

class TodoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoListBinding

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
            TodoListViewModel(repository, Schedulers.io())
        }
        viewModel.listItemsLiveData
            .observe(this, Observer(adapter::submitList))
    }
}