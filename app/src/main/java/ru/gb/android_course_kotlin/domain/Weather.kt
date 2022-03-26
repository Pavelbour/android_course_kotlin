package ru.gb.android_course_kotlin.domain

data class Weather(
        val city: City = getDefaultCity(),
        var temperature: Int? = 0,
        var feelsLike: Int? = -3
)

fun getDefaultCity() = City("New-York", 40.6974034, -74.119763)