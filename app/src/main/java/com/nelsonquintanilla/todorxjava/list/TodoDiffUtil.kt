package com.nelsonquintanilla.todorxjava.list

import androidx.recyclerview.widget.DiffUtil

class TodoDiffUtil : DiffUtil.ItemCallback<TodoListItem>() {
    // Checks to see if two items represent the same item
    override fun areItemsTheSame(oldItem: TodoListItem, newItem: TodoListItem): Boolean {
        return when (oldItem) {
            TodoListItem.DueTasks -> newItem is TodoListItem.DueTasks
            TodoListItem.DoneTasks -> newItem is TodoListItem.DoneTasks
            is TodoListItem.TaskListItem -> {
                if (newItem !is TodoListItem.TaskListItem) return false
                oldItem.taskItem.id == newItem.taskItem.id
            }
        }
    }

    // Checks to see if two items have the same content
    override fun areContentsTheSame(oldItem: TodoListItem, newItem: TodoListItem): Boolean {
        return oldItem == newItem
    }
}