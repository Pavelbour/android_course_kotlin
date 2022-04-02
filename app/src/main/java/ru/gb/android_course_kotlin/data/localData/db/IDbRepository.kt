package ru.gb.android_course_kotlin.data.localData.db

import ru.gb.android_course_kotlin.domain.Weather

interface IDbRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}