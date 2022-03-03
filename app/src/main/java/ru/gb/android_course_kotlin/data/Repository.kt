package ru.gb.android_course_kotlin.data

import ru.gb.android_course_kotlin.data.IRepository
import ru.gb.android_course_kotlin.domain.Weather

class Repository(var weather: Weather = Weather()) : IRepository {

    override fun updateWeather(weather: Weather) {
        this.weather = weather
    }

    override fun getWeatherFromServer(): Weather {
        return weather
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return weather
    }
}