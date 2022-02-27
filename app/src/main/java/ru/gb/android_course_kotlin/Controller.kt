package ru.gb.android_course_kotlin

import ru.gb.android_course_kotlin.domain.Weather

interface Controller {
    fun showDetails(weather: Weather)
}