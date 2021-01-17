package com.allysonjeronimo.doit.ui.task

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allysonjeronimo.doit.R
import com.allysonjeronimo.doit.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository:TaskRepository
) : ViewModel() {

    private val _taskStateEventData = MutableLiveData<TaskState>()

    val taskStateEventData: LiveData<TaskState>
        get() = _taskStateEventData

    private val _messageEventData = MutableLiveData<Int>()

    val messageEventData: LiveData<Int>
        get() = _messageEventData


    fun addOrUpdateTask(id:Long = 0, description: String){
        if(id == 0L){
            addTask(description)
        }
        else{
            updateTask(id, description)
        }
    }

    private fun updateTask(id:Long, description: String) = viewModelScope.launch {
        try{
            repository.update(id, description, false)
            _taskStateEventData.value = TaskState.Updated
            _messageEventData.value = R.string.task_updated_successfully
        }catch(ex: Exception){
            Log.e(TAG, ex.toString())
        }
    }

    private fun addTask(description:String) = viewModelScope.launch {
        try{
            val id = repository.insert(description)
            if(id > 0){
                _taskStateEventData.value = TaskState.Inserted
                _messageEventData.value = R.string.task_inserted_successfully
            }

        }catch(ex: Exception){
            Log.e(TAG, ex.toString())
            _messageEventData.value = R.string.task_error_to_insert
        }
    }

    sealed class TaskState{
        object Inserted: TaskState()
        object Updated: TaskState()
    }

    companion object{
        private val TAG = TaskViewModel::class.simpleName
    }
}