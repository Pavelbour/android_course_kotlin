package ru.gb.android_course_kotlin.data

import ru.gb.android_course_kotlin.domain.Weather

interface IWeatherRepository {
    fun getWeatherListFromLocalStorage(): ArrayList<Weather>
    fun getWeatherListFromServer(): ArrayList<Weather>
    fun addNewItem(weather: Weather)
}