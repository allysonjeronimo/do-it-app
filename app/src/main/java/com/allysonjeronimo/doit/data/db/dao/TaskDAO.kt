package com.allysonjeronimo.doit.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
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
    // usando koltin extensions do Room permite integração com LiveData
    // Assim, ao inserir novos registros, quem estiver ouvindo esse LiveData
    // será notificado
    @Query("SELECT * FROM task")
    suspend fun findAll() :List<Task>
}