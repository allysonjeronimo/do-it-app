package com.allysonjeronimo.doit.ui.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.allysonjeronimo.doit.R
import com.allysonjeronimo.doit.data.db.entity.Task
import kotlinx.android.synthetic.main.task_item.view.*

class TaskListAdapter(
    private val tasks:List<Task>
) : RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)

        return TaskListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size

    class TaskListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val textTaskDescription: TextView = itemView.text_task_description
        private val checkTaskDone:CheckBox = itemView.check_task_done

        fun bind(task: Task){
            textTaskDescription.text = task.description
            checkTaskDone.isChecked = task.done
        }
    }
}