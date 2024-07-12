package com.nelsonquintanilla.todorxjava.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nelsonquintanilla.todorxjava.R

class TodoAdapter : ListAdapter<TodoListItem, RecyclerView.ViewHolder>(TodoDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0,
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_section_header, parent, false)
                TodoSectionViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_todo_item, parent, false)
                TodoViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
}