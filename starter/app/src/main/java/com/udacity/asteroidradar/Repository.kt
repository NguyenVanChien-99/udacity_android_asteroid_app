package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.MyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Repository(private val database: MyDatabase) {
    val asteroids: LiveData<List<Asteroid>> =database.dto.getAsteroids();

    fun getTodayAsteroid(start: String):LiveData<List<Asteroid>> {
        return database.dto.getTodayAsteroids(start)
    }

    fun getWeekAsteroid(start: String,endDate:String):LiveData<List<Asteroid>> {
        return database.dto.getWeekAsteroids(start, endDate)
    }

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay:LiveData<PictureOfDay> = _pictureOfDay


    suspend fun refreshDates(start:String, end:String, apikey:String){

        withContext(Dispatchers.IO){
            val data = parseAsteroidsJsonResult(
                JSONObject(
                    AsteroidApi.service.getAsteroids(
                        start, end, apikey
                    )
                )
            )
            database.dto.delete()
            database.dto.insertAll(data)
        }
    }

    suspend fun getPictureOfDay(apikey:String) {
        val scope = withContext(Dispatchers.IO){
            return@withContext AsteroidApi.service.getPictureOfTheDay(apikey)
        }
        scope.let {
            _pictureOfDay.value = it
        }
    }
}