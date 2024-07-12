package com.nelsonquintanilla.todorxjava.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nelsonquintanilla.todorxjava.R
import com.nelsonquintanilla.todorxjava.utils.buildViewModel

class EditTaskActivity : AppCompatActivity() {

    companion object {
        const val TASK_ID_KEY = "taskIdKey"
        fun launch(context: Context, taskId: Int) {
            val intent = Intent(context, EditTaskActivity::class.java)
            intent.putExtra(TASK_ID_KEY, taskId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        val viewModel = buildViewModel {
            EditTaskViewModel()
        }
    }
}