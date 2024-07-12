package com.nelsonquintanilla.todorxjava.list

import androidx.recyclerview.widget.DiffUtil

class TodoDiffUtil: DiffUtil.ItemCallback<TodoListItem>() {
    override fun areItemsTheSame(oldItem: TodoListItem, newItem: TodoListItem): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: TodoListItem, newItem: TodoListItem): Boolean {
        return false
    }
}