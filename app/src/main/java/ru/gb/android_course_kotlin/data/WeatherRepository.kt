package ru.gb.android_course_kotlin.data

import ru.gb.android_course_kotlin.domain.City
import ru.gb.android_course_kotlin.domain.Weather

class WeatherRepository : IWeatherRepository {
    private val weatherList: ArrayList<Weather> = arrayListOf()

    init {
        weatherList.add(Weather(City("New-York", 40.715097,  -74.003761), 15, 14))
        weatherList.add(Weather(City("Paris", 48.856607, 2.351403),12, 10))
        weatherList.add(Weather(City("Milan", 45.464111, 9.189400),9, 8))

    }

    override fun getWeatherListFromLocalStorage(): ArrayList<Weather> {
        return weatherList
    }

    override fun getWeatherListFromServer(): ArrayList<Weather> {
        return weatherList
    }

    override fun addNewItem(weather: Weather) {
        weatherList.add(weather)
    }
}