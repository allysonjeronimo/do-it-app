package com.allysonjeronimo.doit.ui.task

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allysonjeronimo.doit.R
import com.allysonjeronimo.doit.data.db.entity.Task
import com.allysonjeronimo.doit.repository.TaskRepository
import kotlinx.coroutines.launch

// Controla a View
class TaskViewModel(
    private val repository:TaskRepository
) : ViewModel() {

    // Eventos notificarão a view quando for inserido ou enviará msg de erro,
    // caso ocorra algum
    // Encapsula o MutableLiveData para evitar alterações diretamente
    // na View
    private val _taskStateEventData = MutableLiveData<TaskState>()
    val taskStateEventData: LiveData<TaskState>
        get() = _taskStateEventData

    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int>
        get() = _messageEventData

    // sincronizar com ciclo de vida do android (viewModelScope.launch (LifeCycle)
    fun addTask(description:String) = viewModelScope.launch {
        try{
            val id = repository.insert(description)
            if(id > 0){
                // notifica a view e ela decide como tratar o evento
                _taskStateEventData.value = TaskState.Inserted
                _messageEventData.value = R.string.task_inserted_successfuly
            }

        }catch(ex: Exception){
            Log.e(TAG, ex.toString())
            _messageEventData.value = R.string.task_error_to_insert
        }
    }

    sealed class TaskState{
        object Inserted: TaskState()
    }

    companion object{
        private val TAG = TaskViewModel::class.java.simpleName
    }
}