package ru.gb.android_course_kotlin.data.localData.db

import ru.gb.android_course_kotlin.data.db.HistoryDao
import ru.gb.android_course_kotlin.domain.Weather

class DbRepository(private val dataSource: HistoryDao): IDbRepository {
    override fun getAllHistory(): List<Weather> =
        convertEntityToWeather(dataSource.all())

    override fun saveEntity(weather: Weather) {
        dataSource.insert(convertWeatherToEntity(weather))
    }
}