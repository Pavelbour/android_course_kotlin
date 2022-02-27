package ru.gb.android_course_kotlin.data

import ru.gb.android_course_kotlin.domain.City
import ru.gb.android_course_kotlin.domain.Weather

class WeatherRepository : IWeatherRepository {
    private val weatherList: ArrayList<Weather> = arrayListOf()

    init {
        weatherList.add(Weather(City("New-York", 40.6974034, -74.119763), 15, 14))
        weatherList.add(Weather(City("Paris", 48.8588336, 2.2769958),12, 10))
        weatherList.add(Weather(City("Milan", 45.4627124, 9.107693),9, 8))

    }

    override fun getWeatherListFromLocalStorage(): ArrayList<Weather> {
        return weatherList
    }

    override fun getWeatherListFromServer(): ArrayList<Weather> {
        return weatherList
    }
}