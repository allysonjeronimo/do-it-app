package com.allysonjeronimo.doit.ui.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allysonjeronimo.doit.R
import com.allysonjeronimo.doit.data.db.entity.Task
import com.allysonjeronimo.doit.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _isLoadingEventData = MutableLiveData<Boolean>()
    private val _allTasksEventData = MutableLiveData<List<Task>>()
    private val _messageEventData = MutableLiveData<Int>()

    val allTasksEventData:LiveData<List<Task>>
        get() = _allTasksEventData

    val isLoadingEventData:LiveData<Boolean>
        get() = _isLoadingEventData

    val messageEventData:LiveData<Int>
        get() = _messageEventData

    fun tasks() = viewModelScope.launch {
        try{
            _isLoadingEventData.value = true
            _allTasksEventData.value = taskRepository.findAll()
            _isLoadingEventData.value = false
        }catch(ex:Exception){
            _messageEventData.value = R.string.task_list_error_on_loading
        }
    }
}