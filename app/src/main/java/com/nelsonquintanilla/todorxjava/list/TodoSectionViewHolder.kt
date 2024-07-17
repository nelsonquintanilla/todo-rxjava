package com.nelsonquintanilla.todorxjava.list

import androidx.recyclerview.widget.RecyclerView
import com.nelsonquintanilla.todorxjava.databinding.AdapterSectionHeaderBinding

class TodoSectionViewHolder(
    private val binding: AdapterSectionHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val sectionTitle = binding.sectionTitle
    fun bind(title: String) {
        sectionTitle.text = title
    }
}