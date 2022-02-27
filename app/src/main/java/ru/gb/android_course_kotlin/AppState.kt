package ru.gb.android_course_kotlin

import ru.gb.android_course_kotlin.domain.Weather

sealed class AppState {
    data class Success(val weather: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
