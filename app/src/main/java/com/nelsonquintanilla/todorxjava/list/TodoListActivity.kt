package com.nelsonquintanilla.todorxjava.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelsonquintanilla.todorxjava.R
import com.nelsonquintanilla.todorxjava.utils.buildViewModel

class TodoListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        val adapter = TodoAdapter()
        val recyclerview: RecyclerView = findViewById(R.id.todo_list)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        val viewModel = buildViewModel {
            TodoListViewModel()
        }
    }
}