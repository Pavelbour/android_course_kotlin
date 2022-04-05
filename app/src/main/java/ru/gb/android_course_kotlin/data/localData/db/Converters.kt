package ru.gb.android_course_kotlin.data.localData.db

import ru.gb.android_course_kotlin.data.db.HistoryEntity
import ru.gb.android_course_kotlin.domain.City
import ru.gb.android_course_kotlin.domain.Weather

fun convertEntityToWeather(entityList: List<HistoryEntity>): List<Weather> =
    entityList.map {
        Weather(
            city = City(it.city, 0.0, 0.0),
            temperature = it.temperature,
            feelsLike = 0,
            condition = it.condition
        )
    }

fun convertWeatherToEntity(weather: Weather): HistoryEntity =
    HistoryEntity(
        0,
        weather.city.city,
        weather.temperature,
        weather.condition
    )
