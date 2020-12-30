package com.allysonjeronimo.doit.repository

import com.allysonjeronimo.doit.data.db.dao.TaskDAO
import com.allysonjeronimo.doit.data.db.entity.Task

class DatabaseDataSource(
    private val taskDAO: TaskDAO
) : TaskRepository {

    override suspend fun insert(description: String): Long {
        return taskDAO.insert(Task(description = description))
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

    override suspend fun findAll(): List<Task> {
        return taskDAO.findAll()
    }

}