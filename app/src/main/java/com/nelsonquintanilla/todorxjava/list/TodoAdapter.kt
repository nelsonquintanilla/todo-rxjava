package com.nelsonquintanilla.todorxjava.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nelsonquintanilla.todorxjava.R
import com.nelsonquintanilla.todorxjava.databinding.AdapterSectionHeaderBinding
import com.nelsonquintanilla.todorxjava.databinding.AdapterTodoItemBinding

class TodoAdapter : ListAdapter<TodoListItem, RecyclerView.ViewHolder>(TodoDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0,
            1 -> {
                val binding = AdapterSectionHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TodoSectionViewHolder(binding)
            }

            else -> {
                val binding = AdapterTodoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TodoViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val resources = holder.itemView.context.resources
        when (holder) {
            is TodoSectionViewHolder -> {
                when (item) {
                    TodoListItem.DueTasks -> holder.bind(resources.getString(R.string.due_tasks))
                    TodoListItem.DoneTasks -> holder.bind(resources.getString(R.string.done_tasks))
                    else -> {} // Do nothing for other types
                }
            }

            is TodoViewHolder -> {
                if (item is TodoListItem.TaskListItem) {
                    holder.bind(item.taskItem)
                }
            }
        }
    }
}
