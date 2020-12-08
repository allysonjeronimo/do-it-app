package com.allysonjeronimo.doit.repository

import androidx.lifecycle.LiveData
import com.allysonjeronimo.doit.data.db.entity.Task

interface TaskRepository {
    suspend fun insert(description:String, done:Boolean) : Long
    suspend fun update(id:Long, description:String, done:Boolean)
    suspend fun delete(id:Long)
    suspend fun deleteAll()
    suspend fun findAll() : LiveData<List<Task>>
}