package com.allysonjeronimo.doit.ui.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.allysonjeronimo.doit.R
import com.allysonjeronimo.doit.data.db.AppDatabase
import com.allysonjeronimo.doit.data.db.dao.TaskDAO
import com.allysonjeronimo.doit.data.db.entity.Task
import com.allysonjeronimo.doit.repository.DatabaseDataSource
import com.allysonjeronimo.doit.repository.TaskRepository
import com.allysonjeronimo.doit.ui.task.TaskViewModel
import kotlinx.android.synthetic.main.task_list_fragment.*

class TaskListFragment : Fragment(R.layout.task_list_fragment) {

    private val viewModel: TaskListViewModel by viewModels{
        object:ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val taskDAO: TaskDAO = AppDatabase
                    .getInstance(requireContext())
                    .taskDAO()
                val taskRepository:TaskRepository = DatabaseDataSource(taskDAO)
                return TaskListViewModel(taskRepository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvents()
        setListeners()
    }

    private fun observeEvents() {
        this.viewModel.allTasksEvent.observe(
            this.viewLifecycleOwner,
            Observer { tasks ->
                val taskAdapter = TaskListAdapter(tasks)
                recycler_tasks.run{
                    setHasFixedSize(true)
                    adapter = taskAdapter
                }
            }
        )
    }

    private fun setListeners() {
        this.fab_add_task.setOnClickListener {
            findNavController().navigate(R.id.taskFragment)
        }
    }
}