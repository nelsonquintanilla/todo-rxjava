package com.nelsonquintanilla.todorxjava.list

import androidx.recyclerview.widget.RecyclerView
import com.nelsonquintanilla.todorxjava.databinding.AdapterTodoItemBinding
import com.nelsonquintanilla.todorxjava.model.TaskItem
import io.reactivex.rxjava3.subjects.PublishSubject

class TodoViewHolder(
    private val binding: AdapterTodoItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val taskTitle = binding.taskTitle
    private val taskDone = binding.taskDone

    fun bind(
        taskItem: TaskItem,
        taskClickSubject: PublishSubject<TaskItem>,
        taskToggledSubject: PublishSubject<Pair<TaskItem, Boolean>>
    ) {
        taskTitle.text = taskItem.text
        taskDone.isChecked = taskItem.isDone

        taskDone.setOnClickListener {
            taskToggledSubject.onNext(taskItem to taskDone.isChecked)
        }

        itemView.setOnClickListener {
            taskClickSubject.onNext(taskItem)
        }
    }
}