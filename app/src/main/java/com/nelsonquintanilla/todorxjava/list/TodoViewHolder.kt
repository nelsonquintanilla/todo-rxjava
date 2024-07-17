package com.nelsonquintanilla.todorxjava.list

import androidx.recyclerview.widget.RecyclerView
import com.nelsonquintanilla.todorxjava.databinding.AdapterTodoItemBinding
import com.nelsonquintanilla.todorxjava.model.TaskItem

class TodoViewHolder(
    private val binding: AdapterTodoItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val taskTitle = binding.taskTitle
    private val taskDone = binding.taskDone
    fun bind(item: TaskItem) {
        taskTitle.text = item.text
        taskDone.isChecked = item.isDone
    }
}