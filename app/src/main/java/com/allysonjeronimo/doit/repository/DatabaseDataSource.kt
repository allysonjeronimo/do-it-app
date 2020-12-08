package com.allysonjeronimo.doit.repository

import androidx.lifecycle.LiveData
import com.allysonjeronimo.doit.data.db.dao.TaskDAO
import com.allysonjeronimo.doit.data.db.entity.Task

class DatabaseDataSource(
    private val taskDAO: TaskDAO
) : TaskRepository {

    override suspend fun insert(description: String, done: Boolean): Long {
        return taskDAO.insert(Task(description = description, done = done))
    }

    override suspend fun update(id: Long, description: String, done: Boolean) {
        taskDAO.update(Task(id, description, done))
    }

    override suspend fun delete(id: Long) {
        taskDAO.delete(id)
    }

    override suspend fun deleteAll() {
        taskDAO.deleteAll()
    }

    override suspend fun findAll(): LiveData<List<Task>> {
        return taskDAO.findAll()
    }

}