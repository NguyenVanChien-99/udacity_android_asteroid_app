package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

interface AsteroidService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") apiKey: String
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey: String): PictureOfDay
}

object AsteroidApi {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshiBuilder))
        .build()

    val service = retrofit.create(AsteroidService::class.java)
}