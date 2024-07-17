package com.nelsonquintanilla.todorxjava.list

import com.nelsonquintanilla.todorxjava.model.TaskItem

sealed class TodoListItem(val viewType: Int) {
    data object DueTasks : TodoListItem(0)
    data object DoneTasks : TodoListItem(1)
    data class TaskListItem(val taskItem: TaskItem) : TodoListItem(2)
}