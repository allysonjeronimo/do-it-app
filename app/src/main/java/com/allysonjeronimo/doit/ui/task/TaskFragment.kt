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
import com.allysonjeronimo.doit.R
import com.allysonjeronimo.doit.data.db.AppDatabase
import com.allysonjeronimo.doit.extensions.hideKeyboard
import com.allysonjeronimo.doit.repository.DatabaseDataSource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.task_fragment.*

// Se no método onCreateView não há nada além do inflater,
// podemos passar a view por param no construtor do Fragment
class TaskFragment : Fragment(R.layout.task_fragment) {

    // Se o ViewModel possui parametros, deve-se usar um Factory
    private val viewModel: TaskViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                // resolvendo todas as dependencias de TaskViewModel
                val taskDAO = AppDatabase
                        .getInstance(requireContext())
                        .taskDAO()
                val repository = DatabaseDataSource(taskDAO)
                return TaskViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Ouvir os eventos do ViewModel
        observeEvents()
        // Inicializar listeners de eventos
        setListeners()
    }

    private fun observeEvents() {
        this.viewModel.taskStateEventData.observe(viewLifecycleOwner){taskState ->
            when(taskState){
                TaskViewModel.TaskState.Inserted -> {
                    clearFields()
                    hideKeyboard()
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

    // companion object, newInstance() removido devido o uso do Navigation
    // que fica encarregado de instanciar os fragments

}