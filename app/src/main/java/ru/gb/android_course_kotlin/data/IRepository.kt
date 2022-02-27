package ru.gb.android_course_kotlin.data

import ru.gb.android_course_kotlin.domain.Weather

interface IRepository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}