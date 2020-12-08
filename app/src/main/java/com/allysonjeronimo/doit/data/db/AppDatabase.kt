package com.allysonjeronimo.doit.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.allysonjeronimo.doit.data.db.dao.TaskDAO
import com.allysonjeronimo.doit.data.db.entity.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract val taskDAO : TaskDAO

    companion object{

        @Volatile
        private var INSTANCE:AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()
                }
                return instance
            }
        }
    }
}