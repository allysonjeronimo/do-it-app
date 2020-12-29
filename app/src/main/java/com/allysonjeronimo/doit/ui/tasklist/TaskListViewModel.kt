package com.allysonjeronimo.doit.ui.tasklist

import androidx.lifecycle.ViewModel
import com.allysonjeronimo.doit.repository.TaskRepository

class TaskListViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val allTasksEvent = taskRepository.findAll()

}