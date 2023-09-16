package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.Constants.KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Repository
import com.udacity.asteroidradar.database.DatabaseDAO
import com.udacity.asteroidradar.database.GetDBInstance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    lateinit var startDate: String
    lateinit var endDate: String

    private val database = GetDBInstance(application)
    private val repository = Repository(database)

    val asteroids = repository.asteroids
    val pictureOfTheDay = repository.pictureOfDay

    init {
        refreshData()
    }

    private fun refreshData() {
        val day = Calendar.getInstance()

        val date = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        startDate = date.format(day.time)
        day.add(Calendar.DAY_OF_YEAR, DEFAULT_END_DATE_DAYS)
        endDate = date.format(day.time)

        viewModelScope.launch {
            try {
                repository.refreshDates(startDate, endDate, KEY)
            } catch (error: Exception) {
                Log.i("MainViewModel", "Failed to refresh data, error $error")
            }
            try {
                repository.getPictureOfDay(KEY)
            } catch (error: Exception) {
                Log.e("MainViewModel", "Failed to get picture, error $error")
            }
        }

    }


    val todayAsteroid = repository.getTodayAsteroid(startDate)
    val weelAsteroid = repository.getWeekAsteroid(startDate, endDate)


    private val _navigateToAsteroidData = MutableLiveData<Asteroid?>()
    val navigateToAsteroidData
        get() = _navigateToAsteroidData

    fun onAsteroidClicked(id: Asteroid) {
        //TODO get item form id
        _navigateToAsteroidData.value = id
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroidData.value = null
    }
}