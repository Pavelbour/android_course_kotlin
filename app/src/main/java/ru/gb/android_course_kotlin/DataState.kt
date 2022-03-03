package ru.gb.android_course_kotlin

import ru.gb.android_course_kotlin.domain.Weather

sealed class DataState {
    data class Success(val weather: ArrayList<Weather>) : DataState()
    data class Error(val error: Throwable) : DataState()
    object Loading : DataState()
}