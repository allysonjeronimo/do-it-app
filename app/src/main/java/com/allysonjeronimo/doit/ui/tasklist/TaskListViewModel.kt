package com.allysonjeronimo.doit.ui.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allysonjeronimo.doit.data.db.entity.Task
import com.allysonjeronimo.doit.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _isLoadingEvent = MutableLiveData<Boolean>()
    private val _allTasksEvent = MutableLiveData<List<Task>>()

    val allTasksEvent:LiveData<List<Task>>
        get() = _allTasksEvent

    val isLoadingEvent:LiveData<Boolean>
        get() = _isLoadingEvent

    fun tasks() = viewModelScope.launch {
        _isLoadingEvent.value = true
        _allTasksEvent.value = taskRepository.findAll()
        _isLoadingEvent.value = false
    }
}