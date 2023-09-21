package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface DatabaseDAO {
    @Query("SELECT * FROM Asteroid ORDER BY closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll( asteroids: List<Asteroid>)

    @Query("SELECT * FROM Asteroid WHERE  closeApproachDate = :date")
    fun getTodayAsteroids(date: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate BETWEEN :startDay AND :endDate ORDER BY closeApproachDate DESC")
    fun getWeekAsteroids(startDay: String, endDate: String): LiveData<List<Asteroid>>

    @Query("DELETE FROM Asteroid")
    fun delete()
}