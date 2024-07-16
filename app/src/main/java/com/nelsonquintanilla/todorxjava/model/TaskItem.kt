package com.nelsonquintanilla.todorxjava.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class TaskItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val text: String,
    val addedDate: Date,
    val isDone: Boolean,
)
