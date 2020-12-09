package com.allysonjeronimo.doit.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val description:String,
    val done:Boolean = false
)