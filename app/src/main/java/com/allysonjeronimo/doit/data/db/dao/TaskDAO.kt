package com.allysonjeronimo.doit.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.allysonjeronimo.doit.data.db.entity.Task

@Dao
interface TaskDAO {
    @Insert
    suspend fun insert(task: Task) : Long
    @Update
    suspend fun update(task:Task)
    @Query("DELETE FROM task WHERE id = :id")
    suspend fun delete(id:Long)
    @Query("DELETE FROM task")
    suspend fun deleteAll()
    @Query("SELECT * FROM task")
    suspend fun findAll() : LiveData<List<Task>>
}