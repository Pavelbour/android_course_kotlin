package ru.gb.android_course_kotlin

class WeatherRepository {
    private val weatherList: ArrayList<Weather> = ArrayList()

    fun getWeatherList() = weatherList

    fun addWeather(weather: Weather) {
        weatherList.add(weather)
    }

    fun updateTemperature(temperature: Int, position: Int) {
        weatherList[position] = weatherList[position].copy(temperature = temperature)
    }
}