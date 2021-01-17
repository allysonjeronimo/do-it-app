package com.allysonjeronimo.doit.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName="task")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val description:String,
    val done:Boolean = false
) : Parcelable