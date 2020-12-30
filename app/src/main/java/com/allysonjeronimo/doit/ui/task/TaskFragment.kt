package com.allysonjeronimo.doit.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.allysonjeronimo.doit.R
import com.allysonjeronimo.doit.data.db.AppDatabase
import com.allysonjeronimo.doit.data.db.dao.TaskDAO
import com.allysonjeronimo.doit.extensions.hideKeyboard
import com.allysonjeronimo.doit.repository.DatabaseDataSource
import com.allysonjeronimo.doit.repository.TaskRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.task_fragment.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvents()
        setListeners()
    }

    private fun observeEvents() {

        this.viewModel.taskStateEventData.observe(viewLifecycleOwner){taskState ->
            when(taskState){
                is TaskViewModel.TaskState.Inserted -> {
                    clearFields()
                    hideKeyboard()
                    findNavController().popBackStack()
                }
            }
        }

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
            this.viewModel.addTask(description)
        }
    }
}