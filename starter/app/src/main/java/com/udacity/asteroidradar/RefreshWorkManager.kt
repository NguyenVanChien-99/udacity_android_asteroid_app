package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.GetDBInstance
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.udacity.asteroidradar.Constants.KEY

class RefreshWorkManager (context : Context, Parameter : WorkerParameters):
    CoroutineWorker(context, Parameter) {

    companion object{
        val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {

         var database = GetDBInstance(applicationContext)
        var repository = Repository(database)

        val day = Calendar.getInstance()

        val date = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val start = date.format(day.time)

        day.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
        val end = date.format(day.time)


        return try {
            repository.refreshDates(KEY)
            Result.success()
        }
        catch (error :Exception){
            Result.retry()
        }
    }
}