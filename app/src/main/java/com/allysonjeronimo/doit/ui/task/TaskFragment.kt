package com.allysonjeronimo.doit.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.allysonjeronimo.doit.R
import com.allysonjeronimo.doit.data.db.AppDatabase
import com.allysonjeronimo.doit.data.db.dao.TaskDAO
import com.allysonjeronimo.doit.extensions.hideKeyboard
import com.allysonjeronimo.doit.repository.DatabaseDataSource
import com.allysonjeronimo.doit.repository.TaskRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.task_fragment.*
import kotlinx.android.synthetic.main.task_item.*

class TaskFragment : Fragment(R.layout.task_fragment) {

    private val viewModel: TaskViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val taskDAO: TaskDAO = AppDatabase
                        .getInstance(requireContext())
                        .taskDAO()
                val repository:TaskRepository = DatabaseDataSource(taskDAO)
                return TaskViewModel(repository) as T
            }
        }
    }

    private val args:TaskFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.task?.let { task ->
            button_add_task.text = resources.getString(R.string.task_button_update)
            input_description.setText(task.description)
            button_delete_task.visibility = View.VISIBLE
        }

        observeEvents()
        setListeners()
    }

    private fun observeEvents() {

        this.viewModel.taskStateEventData.observe(viewLifecycleOwner, Observer {taskState ->
            when(taskState){
                is TaskViewModel.TaskState.Inserted,
                is TaskViewModel.TaskState.Updated,
                is TaskViewModel.TaskState.Deleted -> {
                    clearFields()
                    hideKeyboard()
                    requireView().requestFocus()
                    findNavController().popBackStack()
                }
            }
        })

        this.viewModel.messageEventData.observe(viewLifecycleOwner){stringResource ->
            Snackbar.make(requireView(), stringResource, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        input_description.text?.clear()
    }

    private fun hideKeyboard() {
        val activity = requireActivity()
        if(activity is AppCompatActivity){
            activity.hideKeyboard()
        }
    }

    private fun setListeners() {
        button_add_task.setOnClickListener {
            val description = input_description.text.toString()
            this.viewModel.addOrUpdateTask(id = args.task?.id ?: 0L, description = description)
        }
        button_delete_task.setOnClickListener {
            this.viewModel.deleteTask(args.task?.id ?: 0L)
        }
    }
}