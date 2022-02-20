package ru.gb.android_course_kotlin

data class Weather(
        val city : String,
        val temperature : Int,
        val wind : Float,
        val direction: String,
        val default : Boolean
)