package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase(){
    abstract val dto :DatabaseDAO
}

private lateinit var INSTANCE :MyDatabase

fun GetDBInstance(context: Context): MyDatabase{
    synchronized(MyDatabase::class.java){

        if (!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MyDatabase::class.java,
                "AsteroidDatabase"
            ).build()
        }
        return INSTANCE
    }
}