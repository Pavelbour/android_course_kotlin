package ru.gb.android_course_kotlin.data

import ru.gb.android_course_kotlin.data.IRepository
import ru.gb.android_course_kotlin.domain.Weather

class Repository : IRepository {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }
}