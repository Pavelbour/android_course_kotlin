package ru.gb.android_course_kotlin.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v2/informers")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lot: Double
    ): Call<WeatherDTO>
}